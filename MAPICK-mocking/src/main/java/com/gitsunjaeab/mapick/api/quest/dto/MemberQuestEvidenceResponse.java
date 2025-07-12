package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestEvidenceResponse {
    
    // 증빙 ID
    private Long id;
    
    // 증빙 제목
    @NotNull
    private String title;
    
    // 증빙 이미지 URL
    private String evidenceImage;
    
    // 증빙 내용/설명
    private String description;
    
    // 생성일시
    private OffsetDateTime createdAt;
    
    // 수정일시
    private OffsetDateTime updatedAt;
    
    // 삭제일시
    private OffsetDateTime deletedAt;
    
    // 연관된 멤버퀘스트 ID
    private Long memberQuest;
}
