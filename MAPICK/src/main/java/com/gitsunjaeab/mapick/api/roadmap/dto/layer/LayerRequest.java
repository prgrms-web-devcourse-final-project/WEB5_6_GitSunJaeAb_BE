package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LayerRequest {

    @NotNull
    @Size(max = 255)
    private String name;

//    private String description;

    private Integer layerSeq;

    private Long roadmapId;

}
