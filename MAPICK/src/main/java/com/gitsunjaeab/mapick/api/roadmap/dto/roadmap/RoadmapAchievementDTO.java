package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoadmapAchievementDTO {
    private Long roadmapId;
    private boolean achievementUnlocked;
    private AchievementDTO achievement;
}
