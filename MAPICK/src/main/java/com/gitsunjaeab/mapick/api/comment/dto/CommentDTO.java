package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 단일 댓글 DTO
 */

@Getter
@Setter
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private MemberSimpleDTO member;

    @NotNull
    private String content;

    @NotNull
    private OffsetDateTime createdAt;

    private Long roadmap;

    private Long quest;


}
