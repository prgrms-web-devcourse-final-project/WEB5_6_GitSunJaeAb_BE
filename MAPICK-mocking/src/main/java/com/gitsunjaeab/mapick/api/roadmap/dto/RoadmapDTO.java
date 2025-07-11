package com.gitsunjaeab.mapick.api.roadmap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapDTO {

    private Long id;

    @NotNull
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

    @NotNull
    @JsonProperty("isAnimated")
    private Boolean isAnimated;

    private Integer likeCount;

    private Integer viewCount;

    private Integer citationCount; // 인용수

    @NotNull
    @Size(max = 255)
    private String roadmapType;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long member;

    private Long originalRoadmap;
}
