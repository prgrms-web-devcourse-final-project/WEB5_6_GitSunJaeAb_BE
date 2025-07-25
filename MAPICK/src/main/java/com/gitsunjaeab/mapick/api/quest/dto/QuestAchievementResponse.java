package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestAchievementResponse {
    private Long questId;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}
