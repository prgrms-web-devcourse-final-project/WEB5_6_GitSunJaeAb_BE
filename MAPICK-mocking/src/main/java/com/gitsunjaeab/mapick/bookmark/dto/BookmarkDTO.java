package com.gitsunjaeab.mapick.bookmark.dto;

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

    private Long map;

    private Long member;

}
