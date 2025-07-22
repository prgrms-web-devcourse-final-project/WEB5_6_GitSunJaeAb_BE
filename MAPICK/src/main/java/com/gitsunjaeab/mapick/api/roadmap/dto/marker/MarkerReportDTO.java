package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;


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
                .lat(marker.getLat())
                .lng(marker.getLng())
                .color(marker.getColor())
                .imageUrl(marker.getMarkerImage())
                .markerSeq(marker.getMarkerSeq())
                .createdAt(marker.getCreatedAt())
                .updatedAt(marker.getUpdatedAt())
                .deletedAt(marker.getDeletedAt())
                .memberId(marker.getMember().getId())
                .layerId(marker.getLayer().getId())
                .build();
    }

}
