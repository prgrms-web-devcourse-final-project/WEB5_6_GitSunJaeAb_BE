package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestCommentRequest {

    @NotNull
    private String content;  // 댓글 내용

    private Long quest;  // 댓글이 달린 퀘스트 ID

    private Long member;  // 댓글 작성자 ID

} 