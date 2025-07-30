package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.request;

import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class MarkerUpdateRequest implements MarkerRequest{

    @Size(max = 255)
    private String name;

    private String description;

    private String address;

    private Double lat;

    private Double lng;

    @Size(max = 255)
    @Nullable
    private String color;

    @Nullable
    private Long customImageId;

    private Integer markerSeq;
}