package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    List<Roadmap> findAllByIsPublicTrueAndRoadmapType(RoadmapType roadmapType);

    Roadmap findFirstByMember(Member member);

    // 마이페이지 - 회원별 지도 조회용
    List<Roadmap> findByMember(Member member);

//    Roadmap findFirstByOriginalRoadmapAndIdNot(
//        Roadmap roadmap, final Long id);

    List<Roadmap> findAllByIsPublicTrueAndRoadmapTypeAndCategoryId(RoadmapType roadmapType, Long categoryId);

    List<Roadmap> findAllByMember_IdAndDeletedAtIsNull(Long memberId);

    Optional<Roadmap> findByIdAndDeletedAtIsNull(Long roadmapId);
    List<Roadmap> findAllByMemberAndDeletedAtIsNull(Member member);

    Long countByMemberId(Long memberId);
}
