package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapEditorSimpleDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoadmapEditorRepository extends JpaRepository<RoadmapEditor, Long> {

    RoadmapEditor findFirstByRoadmap(Roadmap roadmap);
    RoadmapEditor findFirstByMember(Member member);
    boolean existsByRoadmapIdAndMemberId(Long roadmapId, Long memberId);
    long countByRoadmapIdAndDeletedAtIsNull(Long roadmapId);
    @Query("SELECT new com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapEditorSimpleDTO(" +
            "m.id, m.name, m.nickname, m.profileImage) " +
            "FROM RoadmapEditor re " +
            "JOIN re.member m " +
            "WHERE re.roadmap.id = :roadmapId AND re.deletedAt IS NULL")
    List<RoadmapEditorSimpleDTO> findAllEditorsByRoadmapId(@Param("roadmapId") Long roadmapId);
}
