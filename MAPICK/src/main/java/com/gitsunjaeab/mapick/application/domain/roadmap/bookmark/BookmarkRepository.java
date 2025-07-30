package com.gitsunjaeab.mapick.application.domain.roadmap.bookmark;

import com.gitsunjaeab.mapick.application.domain.member.Member;

import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findFirstByRoadmap(Roadmap roadmap);

    Bookmark findFirstByMember(Member member);

    // 마이페이지 - 회원별 북마크 조회용
    List<Bookmark> findByMemberId(Long memberId);

    List<Bookmark> findAllWithAllRoadmapRelationsByMemberId(Long memberId);

    boolean existsByMemberIdAndRoadmapId(Long memberId, Long roadmapId);

    @Query("SELECT b.roadmap.id FROM Bookmark b WHERE b.member.id = :memberId")
    List<Long> findRoadmapIdsByMemberId(@Param("memberId") Long memberId);
}
