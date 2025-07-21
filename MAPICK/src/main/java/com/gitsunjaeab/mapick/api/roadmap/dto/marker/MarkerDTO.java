package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
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

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    @Size(max = 255)
    private String color;

    @Size(max = 255)
    private String imageUrl;

    private Integer markerSeq;

    private Long layerId;

    public MarkerDTO(Marker marker) {
        this.id = marker.getId();
        this.name = marker.getName();
        this.description = marker.getDescription();
        this.lat = marker.getLat();
        this.lng = marker.getLng();
        this.color = marker.getColor();
        this.imageUrl = marker.getImageUrl();
        this.markerSeq = marker.getMarkerSeq();
        this.layerId = marker.getLayer().getId();
    }
}
