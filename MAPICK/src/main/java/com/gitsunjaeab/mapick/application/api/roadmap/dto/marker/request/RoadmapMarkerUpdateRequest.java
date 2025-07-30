package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RoadmapMarkerUpdateRequest{
    private Long markerId;
    private String name;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private String color;
    private Integer markerSeq;
}
