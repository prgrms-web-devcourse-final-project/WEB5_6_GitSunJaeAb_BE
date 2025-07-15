package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
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

    private MemberSimpleDTO member;

    private Long questId;
}
