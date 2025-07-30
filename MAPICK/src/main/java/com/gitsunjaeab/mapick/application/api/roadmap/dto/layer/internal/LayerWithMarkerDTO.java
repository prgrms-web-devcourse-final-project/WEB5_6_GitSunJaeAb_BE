package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.internal.MarkerDTO;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class LayerWithMarkerDTO {

    private LayerSimpleDTO layer;
    private List<MarkerDTO> markers;

    public LayerWithMarkerDTO(Layer layer) {
        this.layer = new LayerSimpleDTO(layer);
        this.markers = layer.getLayerMarkers().stream()
                .map(MarkerDTO::new)
                .collect(Collectors.toList());
    }
}