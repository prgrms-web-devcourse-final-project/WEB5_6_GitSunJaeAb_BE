package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerDTO {

    private Long id;
    private String name;
    private String description;
    private Integer layerSeq;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private MemberSimpleDTO member; // 레이어 작성자 정보
    private RoadmapSimpleDTO roadmap; // RoadmapDTO -> RoadmapSimpleDTO

    public LayerDTO(Layer layer) {
        this.id = layer.getId();
        this.name = layer.getName();
        this.description = layer.getDescription();
        this.layerSeq = layer.getLayerSeq();
        this.createdAt = layer.getCreatedAt();
        this.updatedAt = layer.getUpdatedAt();
        this.deletedAt = layer.getDeletedAt();
        this.member = layer.getMember() != null ? new MemberSimpleDTO(layer.getMember()) : null; // 레이어 작성자 정보 설정
        this.roadmap = new RoadmapSimpleDTO(layer.getRoadmap()); // RoadmapDTO -> RoadmapSimpleDTO
    }
}