package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LayerZzimDTO {
    
    private Long id;
    private String name;
    private String description;
    private Integer layerSeq;
    private LocalDate layerTime;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private MemberSimpleDTO member; // 레이어 작성자
    private RoadmapSimpleDTO roadmap; // 레이어가 속한 로드맵
    private List<LayerForkHistoryDTO> forkHistories; // 내 로드맵 적용이력

    public LayerZzimDTO(Layer layer) {
        this.id = layer.getId();
        this.name = layer.getName();
        this.description = layer.getDescription();
        this.layerSeq = layer.getLayerSeq();
        this.layerTime = layer.getLayerTime();
        this.createdAt = layer.getCreatedAt();
        this.updatedAt = layer.getUpdatedAt();
        this.deletedAt = layer.getDeletedAt();
        this.member = layer.getMember() != null ? new MemberSimpleDTO(layer.getMember()) : null;
        this.roadmap = new RoadmapSimpleDTO(layer.getRoadmap());
        // forkHistories는 별도로 설정
    }
} 