package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    Roadmap findFirstByMember(Member member);

    Roadmap findFirstByOriginalRoadmapAndIdNot(
        Roadmap roadmap, final Long id);

}
