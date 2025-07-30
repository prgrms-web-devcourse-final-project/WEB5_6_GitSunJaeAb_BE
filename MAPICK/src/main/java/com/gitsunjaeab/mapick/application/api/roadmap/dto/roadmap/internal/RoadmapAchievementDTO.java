package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal;

import com.gitsunjaeab.mapick.application.api.achievement.dto.internal.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoadmapAchievementDTO {
    private Long roadmapId;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}
