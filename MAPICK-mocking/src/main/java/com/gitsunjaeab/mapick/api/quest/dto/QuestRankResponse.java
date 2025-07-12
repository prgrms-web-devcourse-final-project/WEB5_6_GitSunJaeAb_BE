package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestRankResponse {
    
    // 랭킹 ID
    private Long id;
    
    // 순위
    @NotNull
    private Integer rank;
    
    // 점수
    private Integer score;
    
    // 생성일시
    private OffsetDateTime createdAt;
    
    // 수정일시
    private OffsetDateTime updatedAt;
    
    // 연관된 퀘스트 ID
    private Long quest;
    
    // 연관된 멤버 ID
    private Long member;
}
