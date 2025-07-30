package com.gitsunjaeab.mapick.application.api.achievement.dto.internal;

import com.gitsunjaeab.mapick.application.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.application.domain.achievement.MemberAchievement;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 멤버 단일 업적 DTO
 */

@Getter
@AllArgsConstructor
public class MemberAchievementDTO {

    private Long id;
    private String name;
    private String description;
    private String image;
    private OffsetDateTime achievedAt;

    public static MemberAchievementDTO of(MemberAchievement memberAchievement) {
        Achievement achievement = memberAchievement.getAchievement();
        return new MemberAchievementDTO(
            achievement.getId(),
            achievement.getName(),
            achievement.getDescription(),
            achievement.getImage(),
            memberAchievement.getAchievedAt()
        );
    }
}

