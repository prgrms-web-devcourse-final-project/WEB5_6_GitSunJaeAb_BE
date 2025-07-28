package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.OffsetDateTime;
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
    private OffsetDateTime timestamp;
    private List<CommentDTO> comments;

    public static CommentListResponse get(List<CommentDTO> commentDTOList) {
        return new CommentListResponse(
            ResponseCode.OK.getCode(),
            "댓글 목록 조회 성공",
            OffsetDateTime.now(),
            commentDTOList
        );
    }
}

