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

    private String description;

    private String image;

    public AchievementDTO(Achievement achievement) {
        this.id = achievement.getId();
        this.name = achievement.getName();
        this.description = achievement.getDescription();
        this.image = achievement.getImage();
    }

    public static AchievementDTO of(Achievement achievement) {
        return new AchievementDTO(
            achievement.getId(),
            achievement.getName(),
            achievement.getDescription(),
            achievement.getImage()
        );
    }
}
