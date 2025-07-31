package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal;

import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapSimpleDTO {

    private Long id;
    private Long categoryId;
    private String title;
    private Long memberId;
    private String mapType;

    public RoadmapSimpleDTO(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.categoryId = roadmap.getCategory() != null ? roadmap.getCategory().getId() : null;
        this.title = roadmap.getTitle();
        this.memberId = roadmap.getMember() != null ? roadmap.getMember().getId() : null;
        this.mapType = roadmap.getRoadmapType() != null ? roadmap.getRoadmapType().name() : null;
    }

    public static RoadmapSimpleDTO from(Roadmap roadmap) {
        return new RoadmapSimpleDTO(
            roadmap.getId(),
            roadmap.getCategory() != null ? roadmap.getCategory().getId() : null,
            roadmap.getTitle(),
            roadmap.getMember() != null ? roadmap.getMember().getId() : null,
            roadmap.getRoadmapType() != null ? roadmap.getRoadmapType().name() : null
        );
    }
} 