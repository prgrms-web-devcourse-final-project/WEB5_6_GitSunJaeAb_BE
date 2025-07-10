package com.gitsunjaeab.mapick.api.roadmap.dto;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LayerLibraryDTO {

    private Long id;
    private OffsetDateTime createdAt;
    private Long member;
    private Long layer;

}
