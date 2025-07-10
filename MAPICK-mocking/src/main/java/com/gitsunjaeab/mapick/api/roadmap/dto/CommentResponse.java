package com.gitsunjaeab.mapick.api.roadmap.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentResponse {

    private Long id;

    @NotNull
    private String content;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Long roadmap;

    private Long member;

}