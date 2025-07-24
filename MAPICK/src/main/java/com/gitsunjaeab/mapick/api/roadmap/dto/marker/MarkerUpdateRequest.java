package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class MarkerUpdateRequest {

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