package com.gitsunjaeab.mapick.application.api.quest.dto.response;

import com.gitsunjaeab.mapick.application.api.achievement.dto.internal.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestAchievementResponse {
    private Long questId;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}
