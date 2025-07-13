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
    private List<CommentRequest> comments;

    public static CommentListResponse of(List<Comment> commentEntities) {
        List<CommentRequest> commentDTOs = commentEntities.stream()
            .map(c -> new CommentRequest(
                c.getContent(),
                c.getRoadmap().getId()
            )).toList();

        return new CommentListResponse(
            ResponseCode.OK.getCode(),
            "댓글 목록 조회 성공",
            LocalDateTime.now(),
            commentDTOs
        );
    }
}

