package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapEditorRepository extends JpaRepository<RoadmapEditor, Long> {

    RoadmapEditor findFirstByRoadmap(Roadmap roadmap);

    RoadmapEditor findFirstByMember(Member member);

    RoadmapEditor findFirstByInvitedBy(Member member);

}
