package com.gitsunjaeab.mapick.quest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestRequestDTO {

//    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String questImage;

    private String description;

//    @NotNull
//    @JsonProperty("isActive")
//    private Boolean isActive;
//
//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime completedAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;
//
//    private Long member;

}