package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import jakarta.validation.constraints.Size;

public class MarkerSimpleListDto {
    private Long id;

    @Size(max = 255)
    private String name;

    private String description;
}
