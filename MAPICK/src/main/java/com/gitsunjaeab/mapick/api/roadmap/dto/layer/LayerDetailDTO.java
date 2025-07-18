package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LayerDetailDTO {
    private Long id;
    private String name;
    private String description;
    private Integer layerSeq;
    private LocalDate layerTime;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private boolean isZzim;
    private MemberSimpleDTO member;
    private RoadmapDTO roadmap;

    public static LayerDetailDTO from(Layer layer, boolean isZzim) {
        return new LayerDetailDTO(
            layer.getId(),
            layer.getName(),
            layer.getDescription(),
            layer.getLayerSeq(),
            layer.getLayerTime(),
            layer.getCreatedAt(),
            layer.getUpdatedAt(),
            layer.getDeletedAt(),
            isZzim,
            new MemberSimpleDTO(layer.getMember()),
            new RoadmapDTO(layer.getRoadmap())
        );
    }
}
