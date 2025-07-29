package com.gitsunjaeab.mapick.api.quest.dto;


import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;

import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberQuestResponse {



    // 퀘스트 참여 데이터 필드들
    private Long id;

    @NotNull
    private Boolean status;

    private String isRecognized;

    private String title;

    private String answer;

    private OffsetDateTime createdAt;

    private OffsetDateTime completedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private OffsetDateTime submitAt;

    private String imageUrl;

    private String description;

    private MemberSimpleDTO member;

    private Long quest;



    // 퀘스트 참여 생성 응답
    public static MemberQuestResponse ofCreate(MemberQuest memberQuest) {
        return new MemberQuestResponse(
            memberQuest.getId(),
            memberQuest.getStatus(),
            memberQuest.getIsRecognized(),
            memberQuest.getTitle(),
            memberQuest.getAnswer(),
            memberQuest.getCreatedAt(),
            memberQuest.getCompletedAt(),
            memberQuest.getUpdatedAt(),
            memberQuest.getDeletedAt(),
            memberQuest.getSubmitAt(),
            memberQuest.getImageUrl(),
            memberQuest.getDescription(),
            memberQuest.getMember() != null ? new MemberSimpleDTO(memberQuest.getMember()) : null,
            memberQuest.getQuest() != null ? memberQuest.getQuest().getId() : null

            //향후 프론트 요청에 따라 수정 할 예정
            //memberQuest.getQuest() != null ? new QuestDTO(memberQuest.getQuest()) : null

        );
    }

    // 퀘스트 참여 조회 응답 (커스텀 응답 + 데이터)
    public static MemberQuestResponse ofGetDetail(MemberQuestResponse memberQuestResponse) {
        return new MemberQuestResponse(
            memberQuestResponse.getId(),
            memberQuestResponse.getStatus(),
            memberQuestResponse.getIsRecognized(),
            memberQuestResponse.getTitle(),
            memberQuestResponse.getAnswer(),
            memberQuestResponse.getCreatedAt(),
            memberQuestResponse.getCompletedAt(),
            memberQuestResponse.getUpdatedAt(),
            memberQuestResponse.getDeletedAt(),
            memberQuestResponse.getSubmitAt(),
            memberQuestResponse.getImageUrl(),
            memberQuestResponse.getDescription(),
            memberQuestResponse.getMember(),
            memberQuestResponse.getQuest()
        );
    }

    // 퀘스트 참여 목록 조회 응답 (커스텀 응답 + 데이터)
    public static MemberQuestResponse ofGetList(MemberQuestResponse memberQuestResponse) {
        return new MemberQuestResponse(
            memberQuestResponse.getId(),
            memberQuestResponse.getStatus(),
            memberQuestResponse.getIsRecognized(),
            memberQuestResponse.getTitle(),
            memberQuestResponse.getAnswer(),
            memberQuestResponse.getCreatedAt(),
            memberQuestResponse.getCompletedAt(),
            memberQuestResponse.getUpdatedAt(),
            memberQuestResponse.getDeletedAt(),
            memberQuestResponse.getSubmitAt(),
            memberQuestResponse.getImageUrl(),
            memberQuestResponse.getDescription(),
            memberQuestResponse.getMember(),
            memberQuestResponse.getQuest()
        );
    }

}
