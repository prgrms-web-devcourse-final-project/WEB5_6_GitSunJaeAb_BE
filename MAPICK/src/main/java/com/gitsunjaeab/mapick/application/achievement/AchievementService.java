package com.gitsunjaeab.mapick.application.achievement;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.api.achievement.dto.MemberAchievementDTO;
import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievement;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievementRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<MemberAchievementDTO> findMemberAchievements(Long memberId) {
        List<MemberAchievement> memberAchievements = memberAchievementRepository.findAllByMember_Id(memberId);

        return memberAchievements.stream()
            .map(MemberAchievementDTO::of)
            .toList();
    }
}
