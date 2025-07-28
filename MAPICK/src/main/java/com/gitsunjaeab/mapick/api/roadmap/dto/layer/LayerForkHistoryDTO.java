package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerForkHistory;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LayerForkHistoryDTO {
    
    private Long forkedLayerId; // 포크된 레이어 ID
    private String forkedLayerName; // 포크된 레이어 이름
    private RoadmapSimpleDTO forkedRoadmap; // 포크된 로드맵 정보
    private OffsetDateTime forkedAt; // 포크 시간

    public static LayerForkHistoryDTO from(LayerForkHistory forkHistory) {
        return new LayerForkHistoryDTO(
            forkHistory.getForkedLayer().getId(),
            forkHistory.getForkedLayer().getName(),
            new RoadmapSimpleDTO(forkHistory.getForkedLayer().getRoadmap()),
            forkHistory.getForkedAt()
        );
    }
} 