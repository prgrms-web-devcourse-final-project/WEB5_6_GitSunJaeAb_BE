package com.gitsunjaeab.mapick.application.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
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
    private OffsetDateTime timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CommentDTO comment;

    public static CommentResponse create(CommentDTO dto) {
        return new CommentResponse(
            ResponseCode.OK.getCode(),
             "댓글 작성 완료",
            OffsetDateTime.now(),
            dto
        );
    }

    public static CommentResponse createWithAchievement(CommentAchievementDTO commentAchievementDTO) {
        return new CommentResponse(
            ResponseCode.OK.getCode(),
            "댓글 작성 완료! 업적 '" + commentAchievementDTO.getAchievement().getName() + "' 을(를) 획득했습니다.",
            OffsetDateTime.now(),
            commentAchievementDTO.getComment()
        );
    }

    public static CommentResponse get(CommentDTO dto) {
        return new CommentResponse(
            ResponseCode.OK.getCode(),
            "댓글 조회 성공",
            OffsetDateTime.now(),
            dto
        );
    }

    public static CommentResponse update(CommentDTO dto) {
        return new CommentResponse(
            ResponseCode.OK.getCode(),
            "댓글 수정 완료",
            OffsetDateTime.now(),
            dto
        );
    }

    public static CommentResponse delete() {
        return new CommentResponse(
            ResponseCode.OK.getCode(),
            "댓글 삭제 완료",
            OffsetDateTime.now(),
            null
        );
    }
}
