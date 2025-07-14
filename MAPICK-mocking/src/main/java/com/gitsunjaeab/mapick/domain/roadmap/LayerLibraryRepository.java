package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LayerLibraryRepository extends JpaRepository<LayerLibrary, Long> {

    LayerLibrary findFirstByMember(Member member);

    LayerLibrary findFirstByLayer(Layer layer);

    // 회원이 찜한 레이어 Id 조회 (사용자 찜 레이어 목록 조회)
    @Query("SELECT ll.layer.id FROM LayerLibrary ll WHERE ll.member.id = :memberId")
    List<Long> findLayerIdsByMemberId(@Param("memberId") Long memberId);

    // 인용수 조회
    @Query("""
        SELECT l.layer.roadmap.id AS roadmapId, COUNT(DISTINCT l.member.id) AS citationCount
        FROM LayerLibrary l
        WHERE l.layer.roadmap.id IN :roadmapIds
        GROUP BY l.layer.roadmap.id
    """)
    List<RoadmapCitationProjection> countDistinctMemberByRoadmapIds(@Param("roadmapIds") List<Long> roadmapIds);

    List<Layer> findAllByMember_Id(Long memberId);

    interface RoadmapCitationProjection {
        Long getRoadmapId();
        Long getCitationCount();
    }
}
