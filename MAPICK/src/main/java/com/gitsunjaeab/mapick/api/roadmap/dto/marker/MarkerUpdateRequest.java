package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class MarkerUpdateRequest {

    @Size(max = 255)
    private String name;

    private String description;

    private Double lat;

    private Double lng;

    @Size(max = 255)
    private String color;

    private Integer markerSeq;
}