package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 댓글 목록(List) 반환 Response
 */

@Getter
@AllArgsConstructor
public class CommentListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<CommentDTO> comments;

    public static CommentListResponse of(List<Comment> commentEntities) {
        List<CommentDTO> commentLists = commentEntities.stream()
            .map(c -> new CommentDTO(
                c.getId(),
                new MemberSimpleDTO(c.getMember()),
                c.getContent(),
                c.getCreatedAt(),
                c.getRoadmap() == null ? null : c.getRoadmap().getId(),
                c.getQuest() == null ? null : c.getQuest().getId()
            )).toList();

        return new CommentListResponse(
            ResponseCode.OK.getCode(),
            "댓글 목록 조회 성공",
            LocalDateTime.now(),
            commentLists
        );
    }
}

