package com.gitsunjaeab.mapick.application.api.quest.dto.internal;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestRankDTO {

    private Long id;

    @NotNull
    private Integer rank;

    //랭킹 점수
    private Integer score;

    private OffsetDateTime completedAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long quest;

    private Long member;

}
