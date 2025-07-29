package com.gitsunjaeab.mapick.api.roadmap.dto.layer.request;

import lombok.Data;


@Data
public class LayerSyncRequest {
    private String action;           // create / update / delete
    private Long layerTempId;        // 프론트 tempId
    private Long memberId;             // 요청 사용자 ID
    private String name;
    private String description;
    private Integer layerSeq;

    private Long roadmapId;
}