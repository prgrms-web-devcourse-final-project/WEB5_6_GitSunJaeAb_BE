package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestEvidence;
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
public class MemberQuestEvidenceResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;
    
    // 퀘스트 증거 데이터 필드들
    private Long id;
    
    @NotNull
    private String evidenceType;
    
    private String evidenceUrl;
    
    private String description;
    
    private OffsetDateTime createdAt;
    
    private OffsetDateTime updatedAt;
    
    private OffsetDateTime deletedAt;
    
    private Long memberQuest;

    // 퀘스트 증거 생성 응답 
    public static MemberQuestEvidenceResponse ofCreate(MemberQuestEvidence evidence) {
        return new MemberQuestEvidenceResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 증거 생성 완료",
            LocalDateTime.now(),
            evidence.getId(),
            "IMAGE", // 기본값으로 설정
            evidence.getImageUrl(),
            evidence.getDescription(),
            evidence.getCreatedAt(),
            evidence.getUpdatedAt(),
            evidence.getDeletedAt(),
            evidence.getMemberQuest() != null ? evidence.getMemberQuest().getId() : null
        );
    }
}
