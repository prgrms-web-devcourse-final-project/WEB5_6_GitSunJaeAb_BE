package com.gitsunjaeab.mapick.api.quest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class QuestDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String questImage;

    private String description;

    private OffsetDateTime deadline;

    @NotNull
    @JsonProperty("isActive")
    private Boolean isActive;

    private OffsetDateTime createdAt;

    private OffsetDateTime completedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private MemberSimpleDTO member;

    public QuestDTO(Quest quest) {
        this.id = quest.getId();
        this.title = quest.getTitle();
        this.questImage = quest.getQuestImage();
        this.description = quest.getDescription();
        this.deadline = quest.getDeadline();
        this.isActive = quest.getIsActive();
        this.createdAt = quest.getCreatedAt();
        this.completedAt = quest.getCompletedAt();
        this.updatedAt = quest.getUpdatedAt();
        this.deletedAt = quest.getDeletedAt();
        this.member = quest.getMember() != null ? new MemberSimpleDTO(quest.getMember()) : null;
    }
}
