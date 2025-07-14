package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberQuestResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;
    
    // 퀘스트 참여 데이터 필드들
    private Long id;
    
    @NotNull
    private String status;
    
    private String answer;
    
    private String isRecognized;
    
    private OffsetDateTime createdAt;
    
    private OffsetDateTime completedAt;
    
    private OffsetDateTime updatedAt;
    
    private OffsetDateTime deletedAt;
    
//    private Long member;

    private MemberSimpleDTO member;
    private Long quest;

    // 퀘스트 참여 생성 응답 
    public static MemberQuestResponse ofCreate(MemberQuest memberQuest) {
        return new MemberQuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 참여 생성 완료",
            LocalDateTime.now(),
            memberQuest.getId(),
            memberQuest.getStatus(),
            memberQuest.getAnswer(),
            memberQuest.getIsRecognized(),
            memberQuest.getCreatedAt(),
            memberQuest.getCompletedAt(),
            memberQuest.getUpdatedAt(),
            memberQuest.getDeletedAt(),
            memberQuest.getMember() != null ? new MemberSimpleDTO(memberQuest.getMember()) : null,
            memberQuest.getQuest() != null ? memberQuest.getQuest().getId() : null
        );
    }

    // 퀘스트 참여 조회 응답 (커스텀 응답 + 데이터)
    public static MemberQuestResponse ofGetDetail(MemberQuestResponse memberQuestResponse) {
        return new MemberQuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 참여 조회 완료",
            LocalDateTime.now(),
            memberQuestResponse.getId(),
            memberQuestResponse.getStatus(),
            memberQuestResponse.getAnswer(),
            memberQuestResponse.getIsRecognized(),
            memberQuestResponse.getCreatedAt(),
            memberQuestResponse.getCompletedAt(),
            memberQuestResponse.getUpdatedAt(),
            memberQuestResponse.getDeletedAt(),
            memberQuestResponse.getMember(),
            memberQuestResponse.getQuest()
        );
    }

    // 퀘스트 참여 목록 조회 응답 (커스텀 응답 + 데이터)
    public static MemberQuestResponse ofGetList(MemberQuestResponse memberQuestResponse) {
        return new MemberQuestResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 참여 목록 조회 완료",
            LocalDateTime.now(),
            memberQuestResponse.getId(),
            memberQuestResponse.getStatus(),
            memberQuestResponse.getAnswer(),
            memberQuestResponse.getIsRecognized(),
            memberQuestResponse.getCreatedAt(),
            memberQuestResponse.getCompletedAt(),
            memberQuestResponse.getUpdatedAt(),
            memberQuestResponse.getDeletedAt(),
            memberQuestResponse.getMember(),
            memberQuestResponse.getQuest()
        );
    }

}
