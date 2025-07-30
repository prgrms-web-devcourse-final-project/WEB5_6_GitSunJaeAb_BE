package com.gitsunjaeab.mapick.application.api.comment.dto.response;

import com.gitsunjaeab.mapick.application.api.comment.dto.internal.CommentDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.comment.Comment;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 댓글 목록(List) 반환 Response
 */

@Getter
@AllArgsConstructor
public class MemberCommentListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<CommentDTO> comments;

    public static MemberCommentListResponse of(List<Comment> commentEntities) {
        List<CommentDTO> commentLists = commentEntities.stream()
            .map(c -> CommentDTO.builder()
                .id(c.getId())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .roadmap(c.getRoadmap() != null ? c.getRoadmap().getId() : null)
                .quest(c.getQuest() != null ? c.getQuest().getId() : null)
                .build()
            ).toList();

        return new MemberCommentListResponse(
            ResponseCode.OK.getCode(),
            "댓글 목록 조회 성공",
            OffsetDateTime.now(),
            commentLists
        );
    }
}

