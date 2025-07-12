package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestResponse {
    
    // 참여 ID
    private Long id;
    
    // 참여 상태
    @NotNull
    private String status;
    
    // 참여자 답안
    private String answer;
    
    // 인정 여부
    private String isRecognized;
    
    // 생성일시
    private OffsetDateTime createdAt;
    
    // 완료일시
    private OffsetDateTime completedAt;
    
    // 수정일시
    private OffsetDateTime updatedAt;
    
    // 삭제일시
    private OffsetDateTime deletedAt;
    
    // 참여하는 멤버 ID
    private Long member;
    
    // 참여한 퀘스트 ID
    private Long quest;
}
