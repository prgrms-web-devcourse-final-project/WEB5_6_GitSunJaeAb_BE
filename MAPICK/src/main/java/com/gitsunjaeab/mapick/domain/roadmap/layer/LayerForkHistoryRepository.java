package com.gitsunjaeab.mapick.domain.roadmap.layer;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LayerForkHistoryRepository extends JpaRepository<LayerForkHistory, Long> {

    // 1. 특정 원본 레이어의 포크 이력 조회 (특정 사용자가 포크한 것들) + fetch
    @Query("""
        SELECT fh FROM LayerForkHistory fh
        JOIN FETCH fh.forkedLayer
        JOIN FETCH fh.originalLayer
        JOIN FETCH fh.member
        WHERE fh.originalLayer = :originalLayer AND fh.member = :member
    """)
    List<LayerForkHistory> findByOriginalLayerAndMember(@Param("originalLayer") Layer originalLayer, @Param("member") Member member);

    // 2. 사용자가 포크한 모든 레이어 이력 조회 + fetch
    @Query("""
        SELECT fh FROM LayerForkHistory fh
        JOIN FETCH fh.forkedLayer
        JOIN FETCH fh.originalLayer
        JOIN FETCH fh.member
        WHERE fh.member = :member
    """)
    List<LayerForkHistory> findByMember(@Param("member") Member member);

    // 3. 특정 원본 레이어의 모든 포크 이력 조회 + fetch
    @Query("""
        SELECT fh FROM LayerForkHistory fh
        JOIN FETCH fh.forkedLayer
        JOIN FETCH fh.member
        WHERE fh.originalLayer = :originalLayer
    """)
    List<LayerForkHistory> findByOriginalLayer(@Param("originalLayer") Layer originalLayer);

    // 4. 포크된 레이어 ID만 필요하니까 fetch 안 붙임 (지금처럼 그대로 둬도 됨)
    @Query("""
        SELECT fh.forkedLayer.id FROM LayerForkHistory fh
        WHERE fh.originalLayer = :originalLayer AND fh.member = :member
    """)
    List<Long> findForkedLayerIdsByOriginalLayerAndMember(@Param("originalLayer") Layer originalLayer, @Param("member") Member member);

    // 5. LayerId 리스트로 조회할 때 Roadmap과 Category까지 fetch join
    @Query("""
    SELECT fh FROM LayerForkHistory fh
    JOIN FETCH fh.forkedLayer fl
    JOIN FETCH fl.roadmap r
    JOIN FETCH r.category
    WHERE fl.id IN :layerIds
""")
    List<LayerForkHistory> findWithRoadmapAndCategory(@Param("layerIds") List<Long> layerIds);
}