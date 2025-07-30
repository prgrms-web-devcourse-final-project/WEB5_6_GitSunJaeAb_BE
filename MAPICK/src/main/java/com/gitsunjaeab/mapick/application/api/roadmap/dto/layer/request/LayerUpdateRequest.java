package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.request;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.request.MarkerUpdateRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.request.RoadmapMarkerUpdateRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerUpdateRequest {
    private Long layerId;
    private String name;
    private String description;
    private Integer layerSeq;

    private List<RoadmapMarkerUpdateRequest> markers;
}



