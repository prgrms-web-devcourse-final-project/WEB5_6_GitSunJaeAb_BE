package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class SharedRoadmapCreateRequest {
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
    private Double regionLatitude;

    @NotNull
    private Double regionLongitude;

    @NotNull
    private OffsetDateTime participationEnd;

    private List<HashtagRequest> hashtags = new ArrayList<>();
}
