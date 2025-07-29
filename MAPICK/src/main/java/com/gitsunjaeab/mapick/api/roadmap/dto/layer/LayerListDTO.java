package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LayerListDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private Integer layerSeq;

    @NotNull
    private OffsetDateTime createdAt;

    private MemberSimpleDTO member;

    private RoadmapSimpleDTO roadmap;

    // 정적 팩토리 메소드
    public static LayerListDTO of(Layer layer) {
        return LayerListDTO.builder()
            .id(layer.getId())
            .name(layer.getName())
            .description(layer.getDescription())
            .layerSeq(layer.getLayerSeq())
            .createdAt(layer.getCreatedAt())
            .member(layer.getMember() != null ? new MemberSimpleDTO(layer.getMember()) : null)
            .roadmap(layer.getRoadmap() != null ? new RoadmapSimpleDTO(layer.getRoadmap()): null)
            .build();
    }
}
