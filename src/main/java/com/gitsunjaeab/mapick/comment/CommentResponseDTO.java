package com.gitsunjaeab.mapick.comment;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentResponseDTO {

    private Long id;

    @NotNull
    private String content;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Long map;

    private Long member;

}
