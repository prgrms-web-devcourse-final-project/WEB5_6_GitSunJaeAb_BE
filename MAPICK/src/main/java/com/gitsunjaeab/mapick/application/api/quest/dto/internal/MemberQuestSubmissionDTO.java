package com.gitsunjaeab.mapick.application.api.quest.dto.internal;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberQuestSubmissionDTO {
    private String imageUrl;
    private boolean isRecognized;
    private String nickname;
    private OffsetDateTime submittedAt;
    private String title;
    private String description;
    private String profileImage;

}