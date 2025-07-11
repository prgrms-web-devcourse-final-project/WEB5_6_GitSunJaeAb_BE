package com.gitsunjaeab.mapick.api.roadmap.dto;

import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagDTO;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로드맵 목록 반환
 */
@Getter
@AllArgsConstructor
public class RoadmapListDTO {
    private Long id;
    private String categoryName;
    private String title;
    private String description;
    private String thumbnail;
    private List<HashtagDTO> hashtags;
    private Boolean isPublic;
    private RoadmapType roadmapType;
    private Integer likeCount;
    private Integer viewCount;
    private Integer citationCount;
}
