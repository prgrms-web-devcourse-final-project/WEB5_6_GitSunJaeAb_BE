package com.gitsunjaeab.mapick.application.api.quest.dto.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.quest.QuestRank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestRankResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    
    // 퀘스트 랭킹 데이터 필드들
    private Long id;
    
    @NotNull
    private Integer rank;


    private OffsetDateTime completedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    private Long quest;

    // 멤버 정보 - 필요하면 유지
    private Long memberId;
    private String nickname;


    // 퀘스트 랭킹 생성 응답 
    public static QuestRankResponse ofCreate(QuestRank questRank) {

        Member member = questRank.getMember();

        return new QuestRankResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 랭킹 생성 완료",
            OffsetDateTime.now(),
            questRank.getId(),
            questRank.getRank(),
            questRank.getCompletedAt(),
            questRank.getCreatedAt(),
            questRank.getUpdatedAt(),
            questRank.getQuest() != null ? questRank.getQuest().getId() : null,
            member != null ? member.getId() : null,
            member != null ? member.getNickname() : null
        );
    }
}
