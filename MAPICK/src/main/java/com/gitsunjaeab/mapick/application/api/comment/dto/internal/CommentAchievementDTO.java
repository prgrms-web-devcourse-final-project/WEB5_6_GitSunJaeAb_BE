package com.gitsunjaeab.mapick.application.api.comment.dto.internal;

import com.gitsunjaeab.mapick.application.api.achievement.dto.internal.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentAchievementDTO {
    private CommentDTO comment;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}

