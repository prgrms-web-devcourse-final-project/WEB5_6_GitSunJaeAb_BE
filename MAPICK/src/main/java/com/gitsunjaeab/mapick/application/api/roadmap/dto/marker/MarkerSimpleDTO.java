package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarkerSimpleDTO {
    private Long id;
    private String name;
    private Double lat;
    private Double lng;
    private Long layerId;

    public static MarkerSimpleDTO from(Marker marker) {
        return new MarkerSimpleDTO(
            marker.getId(),
            marker.getName(),
            marker.getLat(),
            marker.getLng(),
            marker.getLayer().getId()
        );
    }
}
