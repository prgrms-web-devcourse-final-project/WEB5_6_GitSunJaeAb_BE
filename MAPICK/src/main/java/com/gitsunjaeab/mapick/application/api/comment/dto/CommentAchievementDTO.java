package com.gitsunjaeab.mapick.application.api.comment.dto;

import com.gitsunjaeab.mapick.application.api.achievement.dto.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentAchievementDTO {
    private CommentDTO comment;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}

