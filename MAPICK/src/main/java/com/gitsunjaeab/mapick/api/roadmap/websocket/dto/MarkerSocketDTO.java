package com.gitsunjaeab.mapick.api.roadmap.websocket.dto;

import com.gitsunjaeab.mapick.api.roadmap.websocket.code.MarkerSocketAction;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkerSocketDTO {
    private MarkerSocketAction action;

    private Long markerId;
    private String tempId;

    private String name;
    private String description;
    private String address;
    private String color;

    private Double lat;
    private Double lng;
    private Integer markerSeq;

    private Long layerId;
    private Long memberId;

    private Long customImageId;
    private String tempUUID;

    private Long roadmapId;

    public MarkerSocketDTO(Long markerId, MarkerSocketAction action) {
        this.markerId = markerId;
        this.action = action;
    }

    public MarkerSocketDTO(Marker marker, MarkerSocketAction action) {
        this.markerId = marker.getId();
        this.name = marker.getName();
        this.description = marker.getDescription();
        this.address = marker.getAddress();
        this.lat = marker.getLat();
        this.lng = marker.getLng();
        this.color = marker.getColor();
        this.markerSeq = marker.getMarkerSeq();
        this.layerId = marker.getLayer().getId();
        this.memberId = marker.getMember().getId();
        this.customImageId = marker.getCustomImage() != null ? marker.getCustomImage().getId() : null;
        this.tempUUID = marker.getClientGeneratedUUID();
        this.roadmapId = marker.getLayer().getRoadmap().getId();
        this.action = action;
    }
}
