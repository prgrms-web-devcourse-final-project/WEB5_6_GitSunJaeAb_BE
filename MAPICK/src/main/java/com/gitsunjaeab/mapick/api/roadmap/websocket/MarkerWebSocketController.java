package com.gitsunjaeab.mapick.api.roadmap.websocket;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.api.roadmap.websocket.code.MarkerSocketAction;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.MarkerSocketDTO;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
        MarkerSocketDTO responseDTO;

        switch (dto.getAction()) {
            case ADD -> {
                Marker createdMarker = markerService.createFromSocket(dto);
                responseDTO = new MarkerSocketDTO(createdMarker);
            }
            case UPDATE -> {
                Marker updatedMarker = markerService.updateFromSocket(dto);
                responseDTO = new MarkerSocketDTO(updatedMarker);
            }
            case DELETE -> {
                markerService.delete(dto.getMarkerId());
                responseDTO = new MarkerSocketDTO(dto.getMarkerId(), MarkerSocketAction.DELETE);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 액션입니다: " + dto.getAction());
        }

        // 메시지를 구독한 사용자들에게 전송 (예: /sub/marker/roadmap/{roadmapId})
        String destination = "/sub/marker/roadmap/" + dto.getLayerId(); // 또는 roadmapId 기준으로 변경
        messagingTemplate.convertAndSend(destination, responseDTO);
    }
}