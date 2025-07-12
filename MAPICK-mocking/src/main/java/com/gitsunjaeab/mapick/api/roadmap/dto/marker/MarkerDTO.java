package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long member;

    private Long layer;

    public MarkerDTO(Marker marker) {
        this.id = marker.getId();
        this.name = marker.getName();
        this.description = marker.getDescription();
        this.lat = marker.getLat();
        this.lng = marker.getLng();
        this.color = marker.getColor();
        this.imageUrl = marker.getImageUrl();
        this.markerSeq = marker.getMarkerSeq();
        this.createdAt = marker.getCreatedAt();
        this.updatedAt = marker.getUpdatedAt();
        this.deletedAt = marker.getDeletedAt();
        this.member = marker.getMember() != null ? marker.getMember().getId() : null;
        this.layer = marker.getLayer() != null ? marker.getLayer().getId() : null;
    }

}
