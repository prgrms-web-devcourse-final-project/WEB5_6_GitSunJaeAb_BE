package com.gitsunjaeab.mapick.api.comment.dto;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentAchievementResponse {
    private Long commentId;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}

