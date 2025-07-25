package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;


@Getter
@Setter
public class QuestRequest {

//    private Long id;

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


//    private OffsetDateTime createdAt; // 퀘스트 생성날짜
//
//    private OffsetDateTime completedAt;  // 퀘스트 완료 처리된 시간
//
//    private OffsetDateTime updatedAt;
//
//
//    private OffsetDateTime deletedAt;
//
//
//    private Long member;

}