package com.gitsunjaeab.mapick.domain.achievement;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberAchievementRepository extends JpaRepository<MemberAchievement, Long> {

    boolean existsByMemberIdAndAchievementId(Long memberId, Long achievementId);

    List<MemberAchievement> findAllByMember_Id(Long memberId);
}