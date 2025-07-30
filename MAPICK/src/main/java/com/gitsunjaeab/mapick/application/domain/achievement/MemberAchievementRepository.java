package com.gitsunjaeab.mapick.application.domain.achievement;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberAchievementRepository extends JpaRepository<MemberAchievement, Long> {

    boolean existsByMemberIdAndAchievementId(Long memberId, Long achievementId);

    List<MemberAchievement> findAllByMember_Id(Long memberId);
}