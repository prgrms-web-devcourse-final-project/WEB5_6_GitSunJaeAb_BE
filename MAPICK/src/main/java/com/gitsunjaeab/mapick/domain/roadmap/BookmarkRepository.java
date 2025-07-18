package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findFirstByRoadmap(Roadmap roadmap);

    Bookmark findFirstByMember(Member member);

    // 마이페이지 - 회원별 북마크 조회용
    List<Bookmark> findByMemberId(Long memberId);

    List<Bookmark> findAllWithAllRoadmapRelationsByMemberId(Long memberId);

    boolean existsByMemberAndRoadmap(Member member, Roadmap roadmap);

    Optional<Bookmark> findByMemberAndRoadmap(Member member, Roadmap roadmap);
}
