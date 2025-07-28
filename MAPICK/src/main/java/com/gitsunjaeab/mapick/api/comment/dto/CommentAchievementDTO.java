package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentAchievementDTO {
    private CommentDTO comment;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}

