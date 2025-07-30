package com.gitsunjaeab.mapick.application.api.quest.dto.response;

import com.gitsunjaeab.mapick.application.api.quest.dto.internal.MemberQuestSubmissionDTO;
import com.gitsunjaeab.mapick.application.api.quest.dto.internal.MemberRankingDTO;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestFullDetailResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;

    private QuestResponse quest;
    private List<MemberQuestSubmissionDTO> submission;
    private List<MemberRankingDTO> ranking;

    public static QuestFullDetailResponse of(
        QuestResponse questResponse,
        List<MemberQuestSubmissionDTO> submissions,
        List<MemberRankingDTO> ranking
    ){
        return new QuestFullDetailResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 상세 조회 성공",
            OffsetDateTime.now(),
            questResponse,
            submissions,
            ranking
        );
    }
}
