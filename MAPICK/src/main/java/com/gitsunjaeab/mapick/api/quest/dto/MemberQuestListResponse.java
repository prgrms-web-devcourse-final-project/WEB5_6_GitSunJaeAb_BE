package com.gitsunjaeab.mapick.api.quest.dto;


import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberQuestListResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<MemberQuestResponse> memberQuests;

    public static MemberQuestListResponse of(List<MemberQuestResponse> responses) {
        return new MemberQuestListResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 참여자 목록 조회 완료",
            LocalDateTime.now(),
            responses
        );
    }
}