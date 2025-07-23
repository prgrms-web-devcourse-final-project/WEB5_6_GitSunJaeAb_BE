package com.gitsunjaeab.mapick.application.achievement;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievementRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final MemberAchievementRepository memberAchievementRepository;

    public List<AchievementDTO> findAll() {
        List<Achievement> achievements = achievementRepository.findAll();

        return achievements.stream()
            .map(AchievementDTO::of)
            .toList();
    }

    public List<AchievementDTO> findMemberAchievements(Long memberId) {
        List<Long> achievementIds = memberAchievementRepository.findAchievementIdsByMemberId(memberId);
        List<Achievement> achievements = achievementRepository.findAllById(achievementIds);

        return achievements.stream()
            .map(AchievementDTO::of)
            .toList();
    }
}
