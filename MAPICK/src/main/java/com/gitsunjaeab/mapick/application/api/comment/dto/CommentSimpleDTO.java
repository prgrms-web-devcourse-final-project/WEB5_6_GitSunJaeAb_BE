package com.gitsunjaeab.mapick.application.api.comment.dto;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.comment.Comment;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSimpleDTO {
    private Long id; // 댓글 id
    private String content; // 댓글 내용
    private MemberSimpleDTO member; // 댓글 작성자(발신인)
    private OffsetDateTime createdAt; // 댓글 생성일

    // 엔티티 → DTO 변환 생성자
    public CommentSimpleDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.member = comment.getMember() != null ? new MemberSimpleDTO(comment.getMember()) : null;
        this.createdAt = comment.getCreatedAt();
    }
} 