package com.gitsunjaeab.mapick.api.quest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestResponse {


    // 퀘스트 데이터 필드들
    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String questImage;

    private String description;

    private String hint;

    private OffsetDateTime deadline;


    @NotNull
    @JsonProperty("isActive")
    private Boolean isActive;

    private OffsetDateTime createdAt;

    private OffsetDateTime completedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private MemberSimpleDTO member;

    private Long viewCount;

    public static QuestResponse of(com.gitsunjaeab.mapick.domain.quest.Quest quest) {
        QuestResponse response = new QuestResponse();
        response.setId(quest.getId());
        response.setTitle(quest.getTitle());
        response.setQuestImage(quest.getQuestImage());
        response.setDescription(quest.getDescription());
        response.setHint(quest.getHint());
        response.setDeadline(quest.getDeadline());
        response.setIsActive(quest.getIsActive());
        response.setCreatedAt(quest.getCreatedAt());
        response.setCompletedAt(quest.getCompletedAt());
        response.setUpdatedAt(quest.getUpdatedAt());
        response.setDeletedAt(quest.getDeletedAt());

        // Member -> MemberSimpleDTO 변환
        if (quest.getMember() != null) {
            response.setMember(MemberSimpleDTO.of(quest.getMember()));
        }
        response.setViewCount(quest.getViewCount());
        return response;
    }

    // QuestDTO를 QuestResponse로 변환하는 정적 메서드 (단순 데이터만)
    public static QuestResponse of(QuestDTO questDTO) {
        QuestResponse response = new QuestResponse();
        response.setId(questDTO.getId());
        response.setTitle(questDTO.getTitle());
        response.setQuestImage(questDTO.getQuestImage());
        response.setDescription(questDTO.getDescription());
        response.setHint(questDTO.getHint());
        response.setDeadline(questDTO.getDeadline()); // 마감기한 추가
        response.setIsActive(questDTO.getIsActive());
        response.setCreatedAt(questDTO.getCreatedAt());
        response.setCompletedAt(questDTO.getCompletedAt());
        response.setUpdatedAt(questDTO.getUpdatedAt());
        response.setDeletedAt(questDTO.getDeletedAt());
        response.setMember(questDTO.getMember());
        response.setViewCount(questDTO.getViewCount());
        return response;
    }
    
    // 퀘스트 생성 응답 (커스텀 응답 + 데이터)
    public static QuestResponse ofCreate(QuestResponse questResponse) {
        return new QuestResponse(

            questResponse.getId(),
            questResponse.getTitle(),
            questResponse.getQuestImage(),
            questResponse.getDescription(),
            questResponse.getHint(),
            questResponse.getDeadline(),
            questResponse.getIsActive(),
            questResponse.getCreatedAt(),
            questResponse.getCompletedAt(),
            questResponse.getUpdatedAt(),
            questResponse.getDeletedAt(),
            questResponse.getMember(),
            questResponse.getViewCount()
        );
    }
    
    // 퀘스트 수정 응답 (커스텀 응답 + 데이터)
    public static QuestResponse ofUpdate(QuestResponse questResponse) {
        return new QuestResponse(

            questResponse.getId(),
            questResponse.getTitle(),
            questResponse.getQuestImage(),
            questResponse.getDescription(),
            questResponse.getHint(),
            questResponse.getDeadline(),
            questResponse.getIsActive(),
            questResponse.getCreatedAt(),
            questResponse.getCompletedAt(),
            questResponse.getUpdatedAt(),
            questResponse.getDeletedAt(),
            questResponse.getMember(),
            questResponse.getViewCount()

        );
    }
    
    // 퀘스트 조회 응답 (커스텀 응답 + 데이터)
    public static QuestResponse ofGetDetail(QuestResponse questResponse) {
        return new QuestResponse(

            questResponse.getId(),
            questResponse.getTitle(),
            questResponse.getQuestImage(),
            questResponse.getDescription(),
            questResponse.getHint(),
            questResponse.getDeadline(),
            questResponse.getIsActive(),
            questResponse.getCreatedAt(),
            questResponse.getCompletedAt(),
            questResponse.getUpdatedAt(),
            questResponse.getDeletedAt(),
            questResponse.getMember(),
            questResponse.getViewCount()

        );
    }

    // QuestDTO를 받는 오버로드 메서드들
    public static QuestResponse ofCreate(QuestDTO questDTO) {
        return new QuestResponse(

            questDTO.getId(),
            questDTO.getTitle(),
            questDTO.getQuestImage(),
            questDTO.getDescription(),
            questDTO.getHint(),
            questDTO.getDeadline(),
            questDTO.getIsActive(),
            questDTO.getCreatedAt(),
            questDTO.getCompletedAt(),
            questDTO.getUpdatedAt(),
            questDTO.getDeletedAt(),
            questDTO.getMember(),
            questDTO.getViewCount()

        );
    }
    
    public static QuestResponse ofUpdate(QuestDTO questDTO) {
        return new QuestResponse(

            questDTO.getId(),
            questDTO.getTitle(),
            questDTO.getQuestImage(),
            questDTO.getDescription(),
            questDTO.getHint(),
            questDTO.getDeadline(),
            questDTO.getIsActive(),
            questDTO.getCreatedAt(),
            questDTO.getCompletedAt(),
            questDTO.getUpdatedAt(),
            questDTO.getDeletedAt(),
            questDTO.getMember(),
            questDTO.getViewCount()
        );
    }



}