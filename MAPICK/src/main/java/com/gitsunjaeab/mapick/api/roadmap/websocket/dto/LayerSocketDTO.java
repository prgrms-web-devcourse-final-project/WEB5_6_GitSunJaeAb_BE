package com.gitsunjaeab.mapick.api.roadmap.websocket.dto;

import com.gitsunjaeab.mapick.api.roadmap.websocket.code.LayerSocketAction;
import com.gitsunjaeab.mapick.api.roadmap.websocket.code.MarkerSocketAction;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerSocketDTO {

    private LayerSocketAction action;

    private Long layerId;
    private String tempId;         // 클라이언트 임시 식별자

    private String name;
    private String description;
    private Integer layerSeq;
    private LocalDate layerTime;

    private Long memberId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private Long roadmapId;

    /**
     * 정적 팩토리 메서드 - Layer → LayerSocketDTO 변환
     */
    public static LayerSocketDTO from(Layer layer, LayerSocketAction action) {
        return new LayerSocketDTO(
                action,
                layer.getId(),
                null, // tempId는 클라이언트에서 요청 시 넣으므로 응답에는 그대로 유지 필요
                layer.getName(),
                layer.getDescription(),
                layer.getLayerSeq(),
                layer.getLayerTime(),
                layer.getMember() != null ? layer.getMember().getId() : null,
                layer.getCreatedAt(),
                layer.getUpdatedAt(),
                layer.getRoadmap().getId()
        );
    }
}
