package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestCommentResponse {

    private Long id;

    @NotNull
    private String content;  // 댓글 내용

    @NotNull
    private OffsetDateTime createdAt;  // 댓글 생성 시간

    private OffsetDateTime updatedAt;  // 댓글 수정 시간

    private Long quest;  // 댓글이 달린 퀘스트 ID

    private Long member;  // 댓글 작성자 ID

    private String memberName;  // 댓글 작성자 이름 (추가 정보)

} 