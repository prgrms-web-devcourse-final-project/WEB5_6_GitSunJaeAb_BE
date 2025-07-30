package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.application.api.category.dto.internal.CategorySimpleDTO;
import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.internal.HashtagDTO;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.RoadmapType;

import java.time.OffsetDateTime;
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
    private CategorySimpleDTO category;
    private MemberSimpleDTO member;
    private String title;
    private String description;
    private String thumbnail;
    private List<HashtagDTO> hashtags;
    private Boolean isPublic;
    private RoadmapType roadmapType;
    private Integer likeCount;
    private Integer viewCount;
    private Integer citationCount;
    private OffsetDateTime createdAt;
    // 사용자가 북마크했는지 안 했는지
    private Boolean isBookmarked;
//    private List<LayerListDTO> layers;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long bookmarkId;

}
