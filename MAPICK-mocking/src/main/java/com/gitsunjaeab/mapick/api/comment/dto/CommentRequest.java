package com.gitsunjaeab.mapick.api.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CommentRequest {

    @NotNull
    private String content;


    private Long roadmapId;

    private Long memberId;

}