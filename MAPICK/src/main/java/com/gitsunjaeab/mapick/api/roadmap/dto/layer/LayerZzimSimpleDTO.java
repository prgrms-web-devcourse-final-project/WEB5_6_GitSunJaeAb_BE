package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerSimpleDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerForkHistory;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LayerZzimSimpleDTO {
    
    private Member member;
    private List<Layer> layers;
    private Map<Long, List<LayerForkHistory>> forkHistoriesMap;
    private List<MarkerSimpleDTO> markers;

    public LayerZzimSimpleDTO(
        Member member,
        List<Layer> layers,
        Map<Long, List<LayerForkHistory>> forkHistoriesMap,
        List<MarkerSimpleDTO> markers
    ) {
        this.member = member;
        this.layers = layers;
        this.forkHistoriesMap = forkHistoriesMap;
        this.markers = markers;
    }
} 