package com.gitsunjaeab.mapick.api.roadmap.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookmarkDTO {

    private Long id;

    @NotNull
    private OffsetDateTime createdAt;

    private Long roadmap;

    private Long member;

}
