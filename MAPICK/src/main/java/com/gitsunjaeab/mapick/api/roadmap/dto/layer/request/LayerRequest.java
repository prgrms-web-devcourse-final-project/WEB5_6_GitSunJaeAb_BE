package com.gitsunjaeab.mapick.api.roadmap.dto.layer.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerRequest {

    // 레이어 필드만
    @NotNull
    @Size(max = 255)
    private String name;

    private String description;
    private Integer layerSeq;
    private boolean isZzimed = false;

    private Long originalLayerId;
    private Long roadmapId; // 일반 생성용 로드맵 ID
}


