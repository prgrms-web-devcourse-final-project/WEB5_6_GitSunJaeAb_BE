package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.internal;

import jakarta.validation.constraints.Size;

public class MarkerSimpleListDto {
    private Long id;

    @Size(max = 255)
    private String name;

    private String description;
}
