package com.gitsunjaeab.mapick.api.roadmap.websocket;

import com.gitsunjaeab.mapick.api.roadmap.websocket.code.LayerSocketAction;
import com.gitsunjaeab.mapick.api.roadmap.websocket.code.MarkerSocketAction;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.LayerSocketDTO;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.MarkerSocketDTO;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MarkerWebSocketController {

    private final MarkerService markerService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 실시간 마커 CRUD 메시지 처리
     */
    @MessageMapping("/marker/update") // 예: /app/marker/update
    public void handleMarkerMessage(MarkerSocketDTO dto) {
        if (dto.getAction() == null) {
            throw new IllegalArgumentException("MarkerSocketDTO의 action이 null입니다.");
        }

        MarkerSocketDTO responseDTO;
// 메시지를 구독한 사용자들에게 전송 (예: /sub/marker/roadmap/{roadmapId})
        Long roadmapId = markerService.findRoadmapIdByLayerId(dto.getLayerId());
        String destinationMarker = "/sub/marker/roadmap/" + roadmapId;
        String destinationLayer = "/sub/layer/roadmap/" + roadmapId;

        System.out.println("📥 Received WebSocket DTO: " + dto);
        System.out.println("📤 Sending WebSocket message to: " + destinationMarker);


        switch (dto.getAction()) {
            case ADD -> {
                Marker createdMarker = markerService.createFromSocket(dto);
                responseDTO = new MarkerSocketDTO(createdMarker, MarkerSocketAction.ADD);
                responseDTO.setTempId(dto.getTempId());

                // 레이어의 실시간 상태 동기회
                // 마커는 특정 레이어에 속해 있기에 추가/수정/삭제 되면, 해당 레이어의 상태 변경.

                LayerSocketDTO layerSocketDTO = LayerSocketDTO.from(createdMarker.getLayer(), LayerSocketAction.UPDATE);
                messagingTemplate.convertAndSend(destinationLayer, layerSocketDTO);

            }
            case UPDATE -> {
                Marker updatedMarker = markerService.updateFromSocket(dto);
                responseDTO = new MarkerSocketDTO(updatedMarker, MarkerSocketAction.UPDATE);

                LayerSocketDTO layerSocketDTO = LayerSocketDTO.from(updatedMarker.getLayer(), LayerSocketAction.UPDATE);
                messagingTemplate.convertAndSend(destinationLayer, layerSocketDTO);

            }
            case DELETE -> {
                // 레이어 참조를 위해 먼저 조회
                Marker deletedMarker = markerService.findById(dto.getMarkerId());

                markerService.delete(dto.getMarkerId());
                responseDTO = new MarkerSocketDTO(dto.getMarkerId(), MarkerSocketAction.DELETE);

                // 마커가 사라졌으므로 레이어도 갱신 필요
                LayerSocketDTO layerSocketDTO = LayerSocketDTO.from(deletedMarker.getLayer(), LayerSocketAction.UPDATE);
                messagingTemplate.convertAndSend(destinationLayer, layerSocketDTO);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 액션입니다: " + dto.getAction());
        }

        messagingTemplate.convertAndSend(destinationMarker, responseDTO);
    }
}