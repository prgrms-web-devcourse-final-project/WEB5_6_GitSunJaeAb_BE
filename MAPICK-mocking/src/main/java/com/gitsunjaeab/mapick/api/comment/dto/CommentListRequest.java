package com.gitsunjaeab.mapick.api.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentListRequest {

    private Long id;

    @NotNull
    private String content;

    private Long roadmapId;

    private Long memberId;
}
