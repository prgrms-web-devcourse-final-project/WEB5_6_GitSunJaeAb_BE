package com.gitsunjaeab.mapick.roadmap_editor;

import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.roadmap_editor.entity.RoadmapEditor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapEditorRepository extends JpaRepository<RoadmapEditor, Long> {

    RoadmapEditor findFirstByRoadmap(Roadmap roadmap);

    RoadmapEditor findFirstByMember(Member member);

    RoadmapEditor findFirstByInvitedBy(Member member);

}
