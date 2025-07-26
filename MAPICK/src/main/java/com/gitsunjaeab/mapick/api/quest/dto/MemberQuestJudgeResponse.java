package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.domain.quest.MemberQuest;

public class MemberQuestJudgeResponse {
    private Long id;
    private Long questId;
    private Long memberId;
    private String isRecognized; // "Y" or "N"

    public static MemberQuestJudgeResponse of(MemberQuest memberQuest) {
        MemberQuestJudgeResponse response = new MemberQuestJudgeResponse();
        response.id = memberQuest.getId();
        response.questId = memberQuest.getQuest().getId();
        response.memberId = memberQuest.getMember().getId();
        response.isRecognized = memberQuest.getIsRecognized();
        return response;
    }

}
