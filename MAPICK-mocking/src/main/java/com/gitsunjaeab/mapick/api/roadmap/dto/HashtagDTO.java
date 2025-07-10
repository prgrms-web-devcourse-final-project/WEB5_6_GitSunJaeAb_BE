package com.gitsunjaeab.mapick.api.roadmap.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HashtagDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private OffsetDateTime createdAt;

}
