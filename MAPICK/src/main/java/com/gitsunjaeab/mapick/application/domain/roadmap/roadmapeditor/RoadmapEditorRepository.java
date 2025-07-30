package com.gitsunjaeab.mapick.application.domain.roadmap.roadmapeditor;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapEditorSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoadmapEditorRepository extends JpaRepository<RoadmapEditor, Long> {

    RoadmapEditor findFirstByRoadmap(Roadmap roadmap);
    RoadmapEditor findFirstByMember(Member member);
    boolean existsByRoadmapIdAndMemberId(Long roadmapId, Long memberId);
    long countByRoadmapIdAndDeletedAtIsNull(Long roadmapId);
    @Query("SELECT new com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapEditorSimpleDTO(" +
            "m.id, m.name, m.nickname, m.profileImage) " +
            "FROM RoadmapEditor re " +
            "JOIN re.member m " +
            "WHERE re.roadmap.id = :roadmapId AND re.deletedAt IS NULL")
    List<RoadmapEditorSimpleDTO> findAllEditorsByRoadmapId(@Param("roadmapId") Long roadmapId);

    @Query("""
    SELECT re.roadmap
    FROM RoadmapEditor re
    WHERE re.member.id = :memberId
    AND re.roadmap.roadmapType = 'SHARED'
    AND re.roadmap.isPublic = true
    AND re.roadmap.deletedAt IS NULL
    AND re.deletedAt IS NULL
""")
    List<Roadmap> findParticipatedSharedRoadmaps(@Param("memberId") Long memberId);

}
