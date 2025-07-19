package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "description", "layerSeq", "layerTime", "zzim", "createdAt", "updatedAt", "deletedAt", "member", "roadmap"})
public class LayerDetailDTO {
    private Long id;
    private String name;
    private String description;
    private Integer layerSeq;
    private LocalDate layerTime;
    @JsonProperty("zzim")
    private boolean zzim; // isZzim -> zzim으로 변경
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private MemberSimpleDTO member;
    private RoadmapSimpleDTO roadmap;

    public static LayerDetailDTO from(Layer layer, boolean isZzim) {
        return new LayerDetailDTO(
            layer.getId(),
            layer.getName(),
            layer.getDescription(),
            layer.getLayerSeq(),
            layer.getLayerTime(),
            isZzim, // layerTime 아래로 이동
            layer.getCreatedAt(),
            layer.getUpdatedAt(),
            layer.getDeletedAt(),
            new MemberSimpleDTO(layer.getMember()),
            new RoadmapSimpleDTO(layer.getRoadmap())
        );
    }
}
