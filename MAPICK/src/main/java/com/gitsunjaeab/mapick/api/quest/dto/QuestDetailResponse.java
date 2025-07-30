package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestDetailResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private QuestResponse quest;

    public static QuestDetailResponse of(QuestResponse questResponse) {
        return new QuestDetailResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 조회 완료",
            LocalDateTime.now(),
            questResponse
        );
    }
}