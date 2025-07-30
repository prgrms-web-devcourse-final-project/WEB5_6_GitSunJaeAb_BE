package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkerReportDTO {

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
    private String imageUrl;

    private Integer markerSeq;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long memberId;

    private Long layerId;

    public static MarkerReportDTO of(Marker marker) {
        return MarkerReportDTO.builder()
            .id(marker.getId())
            .name(marker.getName())
            .description(marker.getDescription())
            .address(marker.getAddress())
            .lat(marker.getLat())
            .lng(marker.getLng())
            .color(marker.getColor())
            .imageUrl(marker.getCustomImage() != null ? marker.getCustomImage().getMarkerImage() : null)
            .markerSeq(marker.getMarkerSeq())
            .createdAt(marker.getCreatedAt())
            .updatedAt(marker.getUpdatedAt())
            .deletedAt(marker.getDeletedAt())
            .memberId(marker.getMember().getId())
            .layerId(marker.getLayer().getId())
            .build();
    }
}
