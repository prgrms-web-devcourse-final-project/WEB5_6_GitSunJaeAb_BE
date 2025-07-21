package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerForkHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class LayerZzimSimpleDTO {
    
    private Member member;
    private List<Layer> layers;
    private Map<Long, List<LayerForkHistory>> forkHistoriesMap;

    public LayerZzimSimpleDTO(Member member, List<Layer> layers, Map<Long, List<LayerForkHistory>> forkHistoriesMap) {
        this.member = member;
        this.layers = layers;
        this.forkHistoriesMap = forkHistoriesMap;
    }
} 