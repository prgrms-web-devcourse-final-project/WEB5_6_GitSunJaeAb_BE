package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestRequest {
    
    // 참여할 퀘스트 ID
    @NotNull
    private Long quest;
    
    // 참여하는 멤버 ID
    @NotNull
    private Long member;
}