package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestCommentResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 댓글 데이터 필드들
    private Long id;

    @NotNull
    private String content;  // 댓글 내용

    @NotNull
    private OffsetDateTime createdAt;  // 댓글 생성 시간

    private OffsetDateTime updatedAt;  // 댓글 수정 시간

    private Long quest;  // 댓글이 달린 퀘스트 ID

    private Long member;  // 댓글 작성자 ID

    private String memberName;  // 댓글 작성자 이름 (추가 정보)

    // 퀘스트 댓글 생성 응답 (커스텀 응답 + 데이터)
    public static QuestCommentResponse ofCreate(QuestCommentResponse commentResponse) {
        return new QuestCommentResponse(
            ResponseCode.OK.getCode(),
            "댓글 생성 완료",
            LocalDateTime.now(),
            commentResponse.getId(),
            commentResponse.getContent(),
            commentResponse.getCreatedAt(),
            commentResponse.getUpdatedAt(),
            commentResponse.getQuest(),
            commentResponse.getMember(),
            commentResponse.getMemberName()
        );
    }

} 