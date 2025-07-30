package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.request.HashtagRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.request.LayerUpdateRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoadmapUpdateRequest {

    private String title;
    private String description;
    private Boolean isPublic;
    private Long categoryId;
    private List<String> hashtags;

    private List<LayerUpdateRequest> layers;
}
