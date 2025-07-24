package com.gitsunjaeab.mapick.domain.achievement;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberAchievementRepository extends JpaRepository<MemberAchievement, Long> {

    @Query("select ma.achievement.id from MemberAchievement ma where ma.member.id = :memberId")
    List<Long> findAchievementIdsByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndAchievementId(Long memberId, Long achievementId);
}