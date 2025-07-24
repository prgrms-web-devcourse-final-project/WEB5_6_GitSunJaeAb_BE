package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MarkerCustomImageDTO{

    private Long id;

    private String name;

    private String markerImage;

    public static MarkerCustomImageDTO of(MarkerCustomImage markerCustomImage) {
        return new MarkerCustomImageDTO(
            markerCustomImage.getId(),
            markerCustomImage.getName(),
            markerCustomImage.getMarkerImage()
        );
    }
}
