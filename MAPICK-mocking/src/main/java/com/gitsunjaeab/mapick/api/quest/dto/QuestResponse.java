package com.gitsunjaeab.mapick.api.quest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestResponse {

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

    private Long member;

    // QuestDTO를 QuestResponse로 변환하는 정적 메서드
    public static QuestResponse of(QuestDTO questDTO) {
        QuestResponse response = new QuestResponse();
        response.setId(questDTO.getId());
        response.setTitle(questDTO.getTitle());
        response.setQuestImage(questDTO.getQuestImage());
        response.setDescription(questDTO.getDescription());
        response.setIsActive(questDTO.getIsActive());
        response.setCreatedAt(questDTO.getCreatedAt());
        response.setCompletedAt(questDTO.getCompletedAt());
        response.setUpdatedAt(questDTO.getUpdatedAt());
        response.setDeletedAt(questDTO.getDeletedAt());
        response.setMember(questDTO.getMember());
        return response;
    }

}