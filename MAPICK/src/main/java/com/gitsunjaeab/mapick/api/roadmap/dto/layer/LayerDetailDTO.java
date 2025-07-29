package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;

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
                isZzim, // layerTime 아래로 이동
                layer.getCreatedAt(),
                layer.getUpdatedAt(),
                layer.getDeletedAt(),
                new MemberSimpleDTO(layer.getMember()),
                layer.getRoadmap() != null ? new RoadmapSimpleDTO(layer.getRoadmap()) : null
        );
    }
}
