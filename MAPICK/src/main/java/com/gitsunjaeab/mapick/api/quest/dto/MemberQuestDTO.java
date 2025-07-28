package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberQuestDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private Boolean status;

    @NotNull
    @Size(max = 255)
    private String answer;

    @NotNull
    @Size(max = 255)
    private String isRecognized;

    private OffsetDateTime createdAt;

    private OffsetDateTime completedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long member;

    private Long quest;

}
