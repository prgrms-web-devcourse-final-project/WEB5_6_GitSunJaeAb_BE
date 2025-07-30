package com.gitsunjaeab.mapick.application.api.quest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

//사용자 퀘스트 참여 및 증빙자료 제출
@Getter
@Setter
public class MemberQuestCreateRequest {
    @NotNull
    private Long questId; //내가 참여한 퀘스트가 어떤퀘스트인지에 대한 정보

    @NotBlank
    private String title;    // 증빙 제목
    private String description;    // 증빙 내용/설명


    private String answer; //사용자가 정답 입력

}
