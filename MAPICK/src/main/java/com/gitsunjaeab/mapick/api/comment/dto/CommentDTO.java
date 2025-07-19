package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.domain.comment.Comment;
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

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.member = new MemberSimpleDTO(comment.getMember());
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.roadmap = comment.getRoadmap() != null ? comment.getRoadmap().getId() : null;
        this.quest = comment.getQuest() != null ? comment.getQuest().getId() : null;
    }
}
