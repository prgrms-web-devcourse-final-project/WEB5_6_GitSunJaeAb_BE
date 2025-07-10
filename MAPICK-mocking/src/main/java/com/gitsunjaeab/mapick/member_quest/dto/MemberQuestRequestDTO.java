package com.gitsunjaeab.mapick.member_quest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberQuestRequestDTO {

//    private Long id;

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    @Size(max = 255)
    private String answer;

    @NotNull
    @Size(max = 255)
    private String isRecognized;

//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime completedAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;
//
//    private Long member;
//
//    private Long quest;

}