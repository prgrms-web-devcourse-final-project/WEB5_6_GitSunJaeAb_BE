package com.gitsunjaeab.mapick.api.roadmap.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LayerResponse {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private Integer layerSeq;

    private LocalDate layerTime;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long member;

    private Long roadmap;

}
