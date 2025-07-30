package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.request.HashtagRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoadmapSyncRequest {

    private Long categoryId;
    private Long memberId;

    @NotNull
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull
    @JsonProperty("isPublic")
    private Boolean isPublic;

    private List<HashtagRequest> hashtags = new ArrayList<>();


    private List<LayerRequest> layers;
    private List<Long> deletedLayerIds;
    private List<Long> deletedMarkerIds;

    @Getter
    @Setter
    public static class LayerRequest {
        private Long layerId;        // null이면 새 레이어
        private Long layerTempId;
        private String name;
        private String description;
        private Integer layerSeq;

        private List<MarkerRequest> markers;
    }

    @Getter
    @Setter
    public static class MarkerRequest {
        private Long markerId;
        private Long markerTempId;
        private String name;
        private String description;
        private String address;
        private String color;
        private Double lat;
        private Double lng;
        private Integer markerSeq;
        private Long customImageId;
        private Long layerTempId;
    }
}
