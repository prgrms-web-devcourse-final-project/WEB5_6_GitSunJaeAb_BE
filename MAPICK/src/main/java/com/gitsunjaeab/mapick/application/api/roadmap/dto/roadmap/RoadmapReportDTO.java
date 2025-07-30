package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.RoadmapType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapReportDTO {

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

    private long memberId;

    private Long originalRoadmapId;

    public static RoadmapReportDTO of(Roadmap roadmap) {
        return RoadmapReportDTO.builder()
                .id(roadmap.getId())
                .categoryId(builder().categoryId)
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnail(roadmap.getThumbnail())
                .isPublic(roadmap.getIsPublic())
                .isAnimated(roadmap.getIsAnimated())
                .likeCount(roadmap.getLikeCount())
                .viewCount(roadmap.getViewCount())
                .roadmapType(roadmap.getRoadmapType())
                .createdAt(roadmap.getCreatedAt())
                .updatedAt(roadmap.getUpdatedAt())
                .deletedAt(roadmap.getDeletedAt())
                .memberId(roadmap.getMember().getId())
                .build();
    }
}
