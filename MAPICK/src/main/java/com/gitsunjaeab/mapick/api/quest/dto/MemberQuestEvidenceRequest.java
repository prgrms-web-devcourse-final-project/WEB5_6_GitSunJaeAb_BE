package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestEvidenceRequest {
    
    // 증빙 제목
    @NotNull
    private String title;
    
    // 증빙 내용/설명
    private String description;
    
    // 증빙 이미지 URL
    private String evidenceImage;
    
    // 퀘스트 답변 (증빙 제출 시 필수)
    @NotNull
    private String answer;
    
    // 연관된 멤버퀘스트 ID
    @NotNull
    private Long memberQuest;
}