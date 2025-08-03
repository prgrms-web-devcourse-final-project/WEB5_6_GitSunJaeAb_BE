package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.internal.MarkerSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerForkHistory;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LayerZzimSimpleDTO {
    
    private Member member;
    private List<LayerSimpleDTO> layers;
    private Map<Long, List<LayerForkHistory>> forkHistoriesMap;
    private List<MarkerSimpleDTO> markers;
    private List<RoadmapSimpleDTO> roadmaps;

    public LayerZzimSimpleDTO(
        Member member,
        List<LayerSimpleDTO> layers,
        Map<Long, List<LayerForkHistory>> forkHistoriesMap,
        List<MarkerSimpleDTO> markers,
        List<RoadmapSimpleDTO> roadmaps
    ) {
        this.member = member;
        this.layers = layers;
        this.forkHistoriesMap = forkHistoriesMap;
        this.markers = markers;
        this.roadmaps = roadmaps;
    }
} 