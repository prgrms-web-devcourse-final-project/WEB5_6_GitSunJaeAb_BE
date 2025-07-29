package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
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