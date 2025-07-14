package com.gitsunjaeab.mapick.api.quest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 퀘스트 데이터 필드들
    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String questImage;

    private String description;

    @NotNull
    @JsonProperty("isActive")
    private Boolean isActive;

    private OffsetDateTime createdAt;

    private OffsetDateTime completedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long member;

    // QuestDTO를 QuestResponse로 변환하는 정적 메서드 (단순 데이터만)
    public static QuestResponse of(QuestDTO questDTO) {
        QuestResponse response = new QuestResponse();
        response.setId(questDTO.getId());
        response.setTitle(questDTO.getTitle());
        response.setQuestImage(questDTO.getQuestImage());
        response.setDescription(questDTO.getDescription());
        response.setIsActive(questDTO.getIsActive());
        response.setCreatedAt(questDTO.getCreatedAt());
        response.setCompletedAt(questDTO.getCompletedAt());
        response.setUpdatedAt(questDTO.getUpdatedAt());
        response.setDeletedAt(questDTO.getDeletedAt());
        response.setMember(questDTO.getMember().getId());
        return response;
    }
    
    // 퀘스트 생성 응답 (커스텀 응답 + 데이터)
    public static QuestResponse ofCreate(QuestResponse questResponse) {
        return new QuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 생성 완료",
            LocalDateTime.now(),
            questResponse.getId(),
            questResponse.getTitle(),
            questResponse.getQuestImage(),
            questResponse.getDescription(),
            questResponse.getIsActive(),
            questResponse.getCreatedAt(),
            questResponse.getCompletedAt(),
            questResponse.getUpdatedAt(),
            questResponse.getDeletedAt(),
            questResponse.getMember()
        );
    }
    
    // 퀘스트 수정 응답 (커스텀 응답 + 데이터)
    public static QuestResponse ofUpdate(QuestResponse questResponse) {
        return new QuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 수정 완료",
            LocalDateTime.now(),
            questResponse.getId(),
            questResponse.getTitle(),
            questResponse.getQuestImage(),
            questResponse.getDescription(),
            questResponse.getIsActive(),
            questResponse.getCreatedAt(),
            questResponse.getCompletedAt(),
            questResponse.getUpdatedAt(),
            questResponse.getDeletedAt(),
            questResponse.getMember()
        );
    }
    
    // 퀘스트 조회 응답 (커스텀 응답 + 데이터)
    public static QuestResponse ofGetDetail(QuestResponse questResponse) {
        return new QuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 조회 완료",
            LocalDateTime.now(),
            questResponse.getId(),
            questResponse.getTitle(),
            questResponse.getQuestImage(),
            questResponse.getDescription(),
            questResponse.getIsActive(),
            questResponse.getCreatedAt(),
            questResponse.getCompletedAt(),
            questResponse.getUpdatedAt(),
            questResponse.getDeletedAt(),
            questResponse.getMember()
        );
    }

    // QuestDTO를 받는 오버로드 메서드들
    public static QuestResponse ofCreate(QuestDTO questDTO) {
        return new QuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 생성 완료",
            LocalDateTime.now(),
            questDTO.getId(),
            questDTO.getTitle(),
            questDTO.getQuestImage(),
            questDTO.getDescription(),
            questDTO.getIsActive(),
            questDTO.getCreatedAt(),
            questDTO.getCompletedAt(),
            questDTO.getUpdatedAt(),
            questDTO.getDeletedAt(),
            questDTO.getMember().getId()
        );
    }
    
    public static QuestResponse ofUpdate(QuestDTO questDTO) {
        return new QuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 수정 완료",
            LocalDateTime.now(),
            questDTO.getId(),
            questDTO.getTitle(),
            questDTO.getQuestImage(),
            questDTO.getDescription(),
            questDTO.getIsActive(),
            questDTO.getCreatedAt(),
            questDTO.getCompletedAt(),
            questDTO.getUpdatedAt(),
            questDTO.getDeletedAt(),
            questDTO.getMember().getId()
        );
    }

}