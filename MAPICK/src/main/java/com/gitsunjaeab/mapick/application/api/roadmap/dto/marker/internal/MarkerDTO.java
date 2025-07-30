package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.internal;

import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 단일 마커 DTO
 */

@Getter
@Setter
@AllArgsConstructor
public class MarkerDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    private String description;

    private String address;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    @Size(max = 255)
    private String color;

    @Size(max = 255)
    private MarkerCustomImageDTO customImage;

    private Integer markerSeq;

    private Long layerId;

    public MarkerDTO(Marker marker) {
        this.id = marker.getId();
        this.name = marker.getName();
        this.description = marker.getDescription();
        this.address = marker.getAddress();
        this.lat = marker.getLat();
        this.lng = marker.getLng();
        this.color = marker.getColor();
        this.customImage = marker.getCustomImage() != null
            ? MarkerCustomImageDTO.of(marker.getCustomImage())
            : null;
        this.markerSeq = marker.getMarkerSeq();
        this.layerId = marker.getLayer().getId();
    }


}
