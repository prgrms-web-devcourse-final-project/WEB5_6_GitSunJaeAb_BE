package com.gitsunjaeab.mapick.application.api.quest.dto.response;

import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestJudgeResponse {

    private Long id;
    private Long questId;
    private Long memberId;

    private Boolean isRecognized;

    public static MemberQuestJudgeResponse of(MemberQuest memberQuest) {
        MemberQuestJudgeResponse response = new MemberQuestJudgeResponse();
        response.id = memberQuest.getId();
        response.questId = memberQuest.getQuest().getId();
        response.memberId = memberQuest.getMember().getId();
        response.isRecognized = memberQuest.getIsRecognized();

        return response;
    }
}