package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LayerLibraryRepository extends JpaRepository<LayerLibrary, Long> {

    LayerLibrary findFirstByMember(Member member);

    LayerLibrary findFirstByLayer(Layer layer);

    // 마이페이지 - 회원별 레이어 라이브러리 조회용
    List<LayerLibrary> findByMember(Member member);

    // 인용수 조회
    @Query("""
        SELECT l.layer.roadmap.id AS roadmapId, COUNT(DISTINCT l.member.id) AS citationCount
        FROM LayerLibrary l
        WHERE l.layer.roadmap.id IN :roadmapIds
        GROUP BY l.layer.roadmap.id
    """)
    List<RoadmapCitationProjection> countDistinctMemberByRoadmapIds(@Param("roadmapIds") List<Long> roadmapIds);

    interface RoadmapCitationProjection {
        Long getRoadmapId();
        Long getCitationCount();
    }
}
