package com.gitsunjaeab.mapick.api.roadmap.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MarkerResponse {

    private Long id;

    @Size(max = 255)
    private String title;

    private String description;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    @Size(max = 255)
    private String color;

    @Size(max = 255)
    private String imageUrl;

    private Integer markerSeq;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long member;

    private Long layer;

}
