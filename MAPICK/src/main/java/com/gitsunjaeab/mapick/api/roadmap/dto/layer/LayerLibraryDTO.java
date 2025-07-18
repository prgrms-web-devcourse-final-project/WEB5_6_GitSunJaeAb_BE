package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

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
