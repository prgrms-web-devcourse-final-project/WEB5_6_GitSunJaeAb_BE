package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.CursorPositionDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RealtimeMapController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/roadmap/{roadmapId}/cursor")
    public void handleCursor(@DestinationVariable Long roadmapId, CursorPositionDTO dto) {
        // 유효성 검증, 인증 처리 가능

        messagingTemplate.convertAndSend(
                "/topic/roadmap/" + roadmapId + "/cursor",
                dto
        );
    }
}