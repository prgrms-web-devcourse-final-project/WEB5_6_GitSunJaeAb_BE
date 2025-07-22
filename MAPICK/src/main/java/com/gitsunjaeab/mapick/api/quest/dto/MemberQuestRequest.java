package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestRequest {

    //추후 참여 수정을 분리하는 리팩토링 필요
    // 참여할 퀘스트 ID
//    @NotNull
    private Long quest; // 퀘스트 참여시 필요함
    
    // 참여하는 멤버 ID
//    @NotNull
    private Long member; // 퀘스트 수정시에 필요함

    @NotNull
    private String title;    // 증빙 제목
    private String description;    // 증빙 내용/설명
    private String evidenceImage;    // 증빙 이미지 URL


    // 퀘스트 답변 // ex)정답! 롯데월드~!
    @NotNull
    private String answer;

    // 연관된 멤버퀘스트 ID
    @NotNull
    private Long memberQuest;

}