package com.gitsunjaeab.mapick.application.api.quest.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestJudgeRequest {

    @NotNull
    private Long memberQuestId;

    @NotNull
    private Boolean isRecognized;
}
