package com.gitsunjaeab.mapick.api.roadmap.websocket;

import com.gitsunjaeab.mapick.api.roadmap.websocket.code.LayerSocketAction;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.LayerSocketDTO;
import com.gitsunjaeab.mapick.application.roadmap.LayerService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class LayerWebSocketController {
    private final LayerService layerService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/layer/update")
    public void handleLayerMessage(@Payload LayerSocketDTO dto,
                                   SimpMessageHeaderAccessor headerAccessor) {
        if (dto.getAction() == null) {
            throw new IllegalArgumentException("LayerSocketDTO의 action 값이 null입니다.");
        }
        Long memberId = (Long) headerAccessor.getSessionAttributes().get("memberId");
        if (memberId == null) {
            throw new IllegalArgumentException("WebSocket 세션에 사용자 정보가 없습니다.");
        }
        System.out.println("💡 WebSocket 요청 보낸 사용자 memberId: " + memberId);

        Long roadmapId = dto.getRoadmapId(); // DTO에 roadmapId 포함되어야 함
        String destination = "/sub/layer/roadmap/" + roadmapId;

        System.out.println("📥 Received Layer DTO: " + dto);

        LayerSocketDTO responseDTO;

        switch (dto.getAction()) {
            case ADD -> {
                dto.setMemberId(memberId);
                Layer created = layerService.createFromSocket(dto);
                responseDTO = LayerSocketDTO.from(created, LayerSocketAction.ADD);
                responseDTO.setTempId(dto.getTempId());
            }
            case UPDATE -> {
                dto.setMemberId(memberId);
                Layer updated = layerService.updateFromSocket(dto);
                responseDTO = LayerSocketDTO.from(updated, LayerSocketAction.UPDATE);
            }
            case DELETE -> {
                dto.setMemberId(memberId);
                Layer deleted = layerService.findById(dto.getLayerId());
                layerService.delete(dto.getLayerId(), dto.getMemberId());
                responseDTO = LayerSocketDTO.from(deleted, LayerSocketAction.DELETE);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 액션입니다: " + dto.getAction());
        }

        messagingTemplate.convertAndSend(destination, responseDTO);
    }
}
