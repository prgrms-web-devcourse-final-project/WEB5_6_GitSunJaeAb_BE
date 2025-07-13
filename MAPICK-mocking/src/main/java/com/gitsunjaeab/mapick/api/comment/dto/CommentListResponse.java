package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<CommentListRequest> comments;

    public static CommentListResponse of(List<Comment> commentEntities) {
        List<CommentListRequest> commentDTOs = commentEntities.stream()
            .map(c -> new CommentListRequest(
                c.getId(),
                c.getContent(),
                c.getRoadmap().getId(),
                c.getMember().getId()
            )).toList();

        return new CommentListResponse(
            ResponseCode.OK.getCode(),
            "댓글 목록 조회 성공",
            LocalDateTime.now(),
            commentDTOs
        );
    }
}

