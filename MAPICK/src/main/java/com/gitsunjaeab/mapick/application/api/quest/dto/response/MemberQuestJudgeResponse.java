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
    private Boolean isRecognized; // ← 수정: String → Boolean

    public static MemberQuestJudgeResponse of(MemberQuest memberQuest) {
        MemberQuestJudgeResponse response = new MemberQuestJudgeResponse();
        response.id = memberQuest.getId();
        response.questId = memberQuest.getQuest().getId();
        response.memberId = memberQuest.getMember().getId();

        // "Y"/"N"/null → Boolean 변환
        String raw = memberQuest.getIsRecognized();
        if ("Y".equals(raw)) {
            response.isRecognized = true;
        } else if ("N".equals(raw)) {
            response.isRecognized = false;
        } else {
            response.isRecognized = null;
        }

        return response;
    }
}