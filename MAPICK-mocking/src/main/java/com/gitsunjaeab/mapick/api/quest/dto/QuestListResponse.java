package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class QuestListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 목록 조회용
    @Size(max = 255)
    private List<QuestListItem> quests;

    public QuestListResponse(String code, String message, LocalDateTime timestamp,
        List<QuestListItem> quests) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.quests = quests;
    }

    // 데이터를 같이 반환하는 경우
    public static QuestListResponse of(List<QuestResponse> quests) {
        List<QuestListItem> list = quests.stream()
            .map(q -> new QuestListItem(
                q.getId(),
                q.getTitle(),
                q.getQuestImage(),
                q.getDescription(),
                q.getIsActive(),
                q.getCreatedAt(),
                q.getCompletedAt(),
                q.getUpdatedAt(),
                q.getDeletedAt(),
                q.getMember()
            ))
            .collect(Collectors.toList());

        return new QuestListResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 조회 성공",
            LocalDateTime.now(),
            list
        );
    }

    @Getter
    @AllArgsConstructor
    public static class QuestListItem {
        private Long id;
        private String title;
        private String questImage;
        private String description;
        private Boolean isActive;
        private OffsetDateTime createdAt;
        private OffsetDateTime completedAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime deletedAt;
        private Long member;
    }

    // 데이터 없이 메시지만 반환할 때
    public static QuestListResponse withoutData(ResponseCode responseCode, String message) {
        return new QuestListResponse(
            responseCode.getCode(),
            message,
            LocalDateTime.now(),
            Collections.emptyList()
        );
    }
} 