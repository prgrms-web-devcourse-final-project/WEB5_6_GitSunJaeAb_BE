package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestRankResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;
    
    // 퀘스트 랭킹 데이터 필드들
    private Long id;
    
    @NotNull
    private Integer rank;
    
    private OffsetDateTime completedAt;
    
    private OffsetDateTime createdAt;
    
    private OffsetDateTime updatedAt;
    
    private Long quest;

    // 퀘스트 랭킹 생성 응답 
    public static QuestRankResponse ofCreate(QuestRank questRank) {
        return new QuestRankResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 랭킹 생성 완료",
            LocalDateTime.now(),
            questRank.getId(),
            questRank.getRank(),
            questRank.getCompletedAt(),
            questRank.getCreatedAt(),
            questRank.getUpdatedAt(),
            questRank.getQuest() != null ? questRank.getQuest().getId() : null
        );
    }
}
