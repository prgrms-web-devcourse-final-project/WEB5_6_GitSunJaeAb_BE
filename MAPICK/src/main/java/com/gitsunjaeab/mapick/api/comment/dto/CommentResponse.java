package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 단일 댓글 반환 Response
 */

@Getter
@AllArgsConstructor
public class CommentResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private CommentDTO comment;

    public static CommentResponse of(CommentDTO dto) {
        return new CommentResponse(
            ResponseCode.OK.getCode(),
            "댓글 조회 성공",
            LocalDateTime.now(),
            dto
        );
    }
}
