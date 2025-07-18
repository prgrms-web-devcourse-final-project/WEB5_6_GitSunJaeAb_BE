package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import java.time.LocalDate;
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
    private LocalDate layerTime;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private RoadmapDTO roadmap;

    public LayerDTO(Layer layer) {
        this.id = layer.getId();
        this.name = layer.getName();
        this.description = layer.getDescription();
        this.layerSeq = layer.getLayerSeq();
        this.layerTime = layer.getLayerTime();
        this.createdAt = layer.getCreatedAt();
        this.updatedAt = layer.getUpdatedAt();
        this.deletedAt = layer.getDeletedAt();
        this.roadmap = new RoadmapDTO(layer.getRoadmap());
    }
}