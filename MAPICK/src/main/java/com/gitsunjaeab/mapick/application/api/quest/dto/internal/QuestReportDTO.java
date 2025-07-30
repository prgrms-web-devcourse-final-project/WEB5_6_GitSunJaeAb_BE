package com.gitsunjaeab.mapick.application.api.quest.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.application.domain.quest.Quest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestReportDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String questImage;

    private String description;

    @NotNull
    @JsonProperty("isActive")
    private Boolean isActive;

    private OffsetDateTime createdAt;

    private OffsetDateTime completedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private long memberId;

    public static QuestReportDTO of(Quest quest) {
        return QuestReportDTO.builder()
                .id(quest.getId())
                .title(quest.getTitle())
                .questImage(quest.getQuestImage())
                .description(quest.getDescription())
                .isActive(quest.getIsActive())
                .createdAt(quest.getCreatedAt())
                .completedAt(quest.getCompletedAt())
                .updatedAt(quest.getUpdatedAt())
                .deletedAt(quest.getDeletedAt())
                .memberId(quest.getMember().getId())
                .build();
    }
}
