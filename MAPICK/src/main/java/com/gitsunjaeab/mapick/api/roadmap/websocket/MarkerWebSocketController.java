package com.gitsunjaeab.mapick.api.roadmap.websocket;

import com.gitsunjaeab.mapick.api.roadmap.websocket.code.LayerSocketAction;
import com.gitsunjaeab.mapick.api.roadmap.websocket.code.MarkerSocketAction;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.LayerSocketDTO;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.MarkerSocketDTO;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import com.gitsunjaeab.mapick.application.roadmap.RoadmapEditorService;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MarkerWebSocketController {

    private final MarkerService markerService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoadmapEditorService roadmapEditorService;

    /**
     * 실시간 마커 CRUD 메시지 처리
     */
    @MessageMapping("/marker/update") // 예: /app/marker/update
    public void handleMarkerMessage(MarkerSocketDTO dto,
                                    SimpMessageHeaderAccessor headerAccessor) {
        if (dto.getAction() == null) {
            throw new IllegalArgumentException("MarkerSocketDTO의 action이 null입니다.");
        }
        Long memberId = (Long) headerAccessor.getSessionAttributes().get("memberId");
        if (memberId == null) {
            throw new IllegalArgumentException("WebSocket 세션에 사용자 정보가 없습니다.");
        }
        System.out.println("💡 WebSocket 요청 보낸 사용자 memberId: " + memberId);

        // 메시지를 구독한 사용자들에게 전송 (예: /sub/marker/roadmap/{roadmapId})
        Long roadmapId = markerService.findRoadmapIdByLayerId(dto.getLayerId());
        String destinationMarker = "/sub/marker/roadmap/" + roadmapId;
        String destinationLayer = "/sub/layer/roadmap/" + roadmapId;

        System.out.println("📥 Received Marker DTO: " + dto);

        MarkerSocketDTO responseDTO;

        dto.setMemberId(memberId);

        switch (dto.getAction()) {
            case ADD -> {
                Marker createdMarker = markerService.createFromSocket(dto);
                Marker fetchedMarker = markerService.findByIdWithLayerAndRoadmap(createdMarker.getId());
                responseDTO = new MarkerSocketDTO(fetchedMarker, MarkerSocketAction.ADD);
                responseDTO.setTempId(dto.getTempId());

                // 레이어의 실시간 상태 동기회
                // 마커는 특정 레이어에 속해 있기에 추가/수정/삭제 되면, 해당 레이어의 상태 변경.
                LayerSocketDTO layerSocketDTO = LayerSocketDTO.from(createdMarker.getLayer(), LayerSocketAction.UPDATE);
                messagingTemplate.convertAndSend(destinationLayer, layerSocketDTO);

                roadmapEditorService.registerEditorIfNotExists(roadmapId, memberId);

            }
            case UPDATE -> {
                Marker updatedMarker = markerService.updateFromSocket(dto);
                Marker fetchedMarker = markerService.findByIdWithLayerAndRoadmap(updatedMarker.getId());
                responseDTO = new MarkerSocketDTO(fetchedMarker, MarkerSocketAction.UPDATE);
                responseDTO.setTempId(dto.getTempId());

                LayerSocketDTO layerSocketDTO = LayerSocketDTO.from(updatedMarker.getLayer(), LayerSocketAction.UPDATE);
                messagingTemplate.convertAndSend(destinationLayer, layerSocketDTO);

                roadmapEditorService.registerEditorIfNotExists(roadmapId, memberId);
            }
            case DELETE -> {
                Marker deletedMarker = markerService.findByIdWithLayerAndRoadmap(dto.getMarkerId());

                markerService.delete(dto.getMarkerId());
                responseDTO = new MarkerSocketDTO(dto.getMarkerId(), MarkerSocketAction.DELETE);

                LayerSocketDTO layerSocketDTO = LayerSocketDTO.from(deletedMarker.getLayer(), LayerSocketAction.UPDATE);
                messagingTemplate.convertAndSend(destinationLayer, layerSocketDTO);

                roadmapEditorService.registerEditorIfNotExists(roadmapId, memberId);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 액션입니다: " + dto.getAction());
        }

        messagingTemplate.convertAndSend(destinationMarker, responseDTO);
    }
}