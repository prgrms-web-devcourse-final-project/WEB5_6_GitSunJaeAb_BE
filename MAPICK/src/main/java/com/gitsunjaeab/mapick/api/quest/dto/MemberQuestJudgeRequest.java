package com.gitsunjaeab.mapick.api.quest.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestJudgeRequest {

    private Long memberQuestId;
    private Boolean isRecognized;
}
