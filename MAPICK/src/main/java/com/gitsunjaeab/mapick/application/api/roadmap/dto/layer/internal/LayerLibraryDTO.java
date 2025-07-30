package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LayerLibraryDTO {

    private Long id;
    private Long member;
    private Long layer;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

}
