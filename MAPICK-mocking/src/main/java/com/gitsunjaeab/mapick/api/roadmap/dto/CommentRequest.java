package com.gitsunjaeab.mapick.api.roadmap.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentRequest {

//    private Long id;

    @NotNull
    private String content;

//    @NotNull
//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime updatedAt;
//
//    private Long map;
//
//    private Long member;

}