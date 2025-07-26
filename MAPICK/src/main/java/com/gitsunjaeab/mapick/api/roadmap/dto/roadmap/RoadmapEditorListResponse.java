package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoadmapEditorListResponse {
    private List<RoadmapEditorSimpleDTO> editors;
    private long count;
}
