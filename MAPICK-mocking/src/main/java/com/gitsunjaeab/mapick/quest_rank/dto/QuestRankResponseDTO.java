package com.gitsunjaeab.mapick.quest_rank.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestRankResponseDTO {

    private Long id;

    @NotNull
    private Integer rank;

    private OffsetDateTime completedAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long quest;

    private Long member;

}
