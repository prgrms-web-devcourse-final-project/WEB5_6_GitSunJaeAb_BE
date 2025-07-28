package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap;

import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
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
    private Long memberId; // 만든사람 id만 포함

    public RoadmapSimpleDTO(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.categoryId = roadmap.getCategory() != null ? roadmap.getCategory().getId() : null;
        this.title = roadmap.getTitle();
        this.memberId = roadmap.getMember() != null ? roadmap.getMember().getId() : null;
    }
} 