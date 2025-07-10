package com.gitsunjaeab.mapick.roadmap;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    Roadmap findFirstByMember(Member member);

    Roadmap findFirstByOriginalRoadmapAndIdNot(
        Roadmap roadmap, final Long id);

}
