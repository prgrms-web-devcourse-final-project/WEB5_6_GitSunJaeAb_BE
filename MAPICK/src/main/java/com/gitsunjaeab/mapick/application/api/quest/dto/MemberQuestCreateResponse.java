package com.gitsunjaeab.mapick.application.api.quest.dto;

import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestCreateResponse {
    private Long id;
    private Long questId;
    private Long memberId;
    private OffsetDateTime submitAt;

    public static MemberQuestCreateResponse of(MemberQuest entity) {
        MemberQuestCreateResponse response = new MemberQuestCreateResponse();
        response.setId(entity.getId());
        response.setQuestId(entity.getQuest().getId());
        response.setMemberId(entity.getMember().getId());
        response.setSubmitAt(entity.getSubmitAt());
        return response;
    }
}