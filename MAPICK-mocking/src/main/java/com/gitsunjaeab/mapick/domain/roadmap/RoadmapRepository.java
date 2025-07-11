package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    List<Roadmap> findAllByIsPublicTrueAndRoadmapType(RoadmapType roadmapType);

    Roadmap findFirstByMember(Member member);

    Roadmap findFirstByOriginalRoadmapAndIdNot(
        Roadmap roadmap, final Long id);

}
