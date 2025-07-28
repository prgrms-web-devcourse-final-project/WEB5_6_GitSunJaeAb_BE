package com.gitsunjaeab.mapick.api.quest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;



//점수를 입력해서 부여하는 방식이 아니기 때문에 안쓰일거라 예상
@Getter
@Setter
public class QuestRankRequest {

//    private Long id;

    @NotNull
    private Integer rank;

//    private OffsetDateTime completedAt;
//
//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;
//
//    private Long quest;
//
//    private Long member;

}