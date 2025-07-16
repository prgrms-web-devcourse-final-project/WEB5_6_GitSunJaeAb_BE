package com.gitsunjaeab.mapick.api.roadmap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoadmapRequest {

    private Long categoryId;

    @NotNull
    @Size(max = 255)
    private String title;

    private String description;

    @Size(max = 255)
    private String thumbnail;

    @NotNull
    @JsonProperty("isPublic")
    private Boolean isPublic;

//    @NotNull
//    @JsonProperty("isAnimated")
//    private Boolean isAnimated;

//    private Integer likeCount;
//
//    private Integer viewCount;

    // 아래 항목은 전부 로직에서 자동 처리
//    @NotNull
//    @Size(max = 255)
//    private String roadmapType;

//    @NotNull
//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;

//    private Long member;
}
