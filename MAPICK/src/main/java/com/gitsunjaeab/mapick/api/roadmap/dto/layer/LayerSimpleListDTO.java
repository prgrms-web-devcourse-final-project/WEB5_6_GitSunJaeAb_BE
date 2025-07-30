package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LayerSimpleListDTO {
    private Long id;
    private String name;
    private String description;
//    private List<MarkerListDTO> markers;
}
