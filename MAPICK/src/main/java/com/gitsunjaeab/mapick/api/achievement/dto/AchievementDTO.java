package com.gitsunjaeab.mapick.api.achievement.dto;

import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 단일 업적 DTO
 */

@Getter
@AllArgsConstructor
public class AchievementDTO {

    private Long id;

    private String name;

    private String image;

    public static AchievementDTO of(Achievement achievement) {
        return new AchievementDTO(
            achievement.getId(),
            achievement.getName(),
            achievement.getImage()
        );
    }
}
