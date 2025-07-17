package com.gitsunjaeab.mapick.api.roadmap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
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
    private RoadmapType roadmapType;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private MemberSimpleDTO member;

    private Long originalRoadmap;

    public RoadmapDTO(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.categoryId = roadmap.getCategory() != null ? roadmap.getCategory().getId() : null;
        this.title = roadmap.getTitle();
        this.description = roadmap.getDescription();
        this.thumbnail = roadmap.getThumbnail();
        this.isPublic = roadmap.getIsPublic();
        this.isAnimated = roadmap.getIsAnimated();
        this.likeCount = roadmap.getLikeCount();
        this.viewCount = roadmap.getViewCount();
        this.citationCount = 0; // 필요에 따라 값을 설정하세요
        this.roadmapType = roadmap.getRoadmapType();
        this.createdAt = roadmap.getCreatedAt();
        this.updatedAt = roadmap.getUpdatedAt();
        this.deletedAt = roadmap.getDeletedAt();
        this.member = roadmap.getMember() != null ? new MemberSimpleDTO(roadmap.getMember()) : null;
    }
}
