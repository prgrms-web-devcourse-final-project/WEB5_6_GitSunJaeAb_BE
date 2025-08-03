package com.gitsunjaeab.mapick.application.domain.roadmap.roadmap;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    List<Roadmap> findAllByIsPublicTrueAndRoadmapTypeAndDeletedAtIsNull(RoadmapType roadmapType);

    Roadmap findFirstByMember(Member member);

    List<Roadmap> findAllByIsPublicTrueAndRoadmapTypeAndCategoryIdAndDeletedAtIsNull(RoadmapType roadmapType, Long categoryId);

    List<Roadmap> findAllByMember_IdAndDeletedAtIsNull(Long memberId);

    Optional<Roadmap> findByIdAndDeletedAtIsNull(Long roadmapId);

    Long countByMemberId(Long memberId);

    Long countByMemberIdAndRoadmapType(Long memberId, RoadmapType roadmapType);
}
