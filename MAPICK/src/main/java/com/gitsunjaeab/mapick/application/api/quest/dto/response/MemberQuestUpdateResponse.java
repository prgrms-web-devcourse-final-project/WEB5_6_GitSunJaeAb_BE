package com.gitsunjaeab.mapick.application.api.quest.dto.response;


import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuestUpdateResponse {

    private Long id;
    private String title;
    private String answer;
    private String imageUrl;
    private String description;
    private OffsetDateTime updatedAt;

    public static MemberQuestUpdateResponse of(MemberQuest memberQuest) {
        MemberQuestUpdateResponse response = new MemberQuestUpdateResponse();
        response.setId(memberQuest.getId());
        response.setTitle(memberQuest.getTitle());
        response.setAnswer(memberQuest.getAnswer());
        response.setImageUrl(memberQuest.getImageUrl());
        response.setDescription(memberQuest.getDescription());
        response.setUpdatedAt(memberQuest.getUpdatedAt());
        return response;
    }
}