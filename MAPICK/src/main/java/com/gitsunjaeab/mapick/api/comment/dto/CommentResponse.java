package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 반환 Response
 */

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {

    private Long id;

    private MemberSimpleDTO member;

    @NotNull
    private String content;

    private OffsetDateTime createdAt;

    private Long roadmapId;

    private Long questId;
}