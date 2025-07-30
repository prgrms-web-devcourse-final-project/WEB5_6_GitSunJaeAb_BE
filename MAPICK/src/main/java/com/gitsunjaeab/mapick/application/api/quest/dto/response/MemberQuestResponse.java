package com.gitsunjaeab.mapick.application.api.quest.dto.response;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;

import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
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

    private Long id;

    @NotNull
    private Boolean status;

    // Boolean 타입 유지
    private Boolean isRecognized;

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

    // 공통 변환 메서드
    public static MemberQuestResponse from(MemberQuest memberQuest) {
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
        );
    }

    // 이름만 다른 중복 메서드는 제거 가능하지만, 필요하다면 유지
//    public static MemberQuestResponse ofCreate(MemberQuest memberQuest) {
//        return from(memberQuest);
//    }
//
//    public static MemberQuestResponse ofGetDetail(MemberQuest memberQuest) {
//        return from(memberQuest);
//    }

    public static MemberQuestResponse ofGetList(MemberQuest memberQuest) {
        return from(memberQuest);
    }
}
