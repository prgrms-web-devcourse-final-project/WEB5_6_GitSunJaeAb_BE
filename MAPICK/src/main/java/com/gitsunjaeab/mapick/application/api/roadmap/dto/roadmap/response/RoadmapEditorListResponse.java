package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.response;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.RoadmapEditorSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoadmapEditorListResponse {
    private List<RoadmapEditorSimpleDTO> editors;
    private long count;
}
