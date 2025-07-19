package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LayerForkHistoryRepository extends JpaRepository<LayerForkHistory, Long> {

    // 특정 원본 레이어의 포크 이력 조회 (특정 사용자가 포크한 것들)
    @Query("SELECT fh FROM LayerForkHistory fh WHERE fh.originalLayer = :originalLayer AND fh.member = :member")
    List<LayerForkHistory> findByOriginalLayerAndMember(@Param("originalLayer") Layer originalLayer, @Param("member") Member member);

    // 사용자가 포크한 모든 레이어 이력 조회
    @Query("SELECT fh FROM LayerForkHistory fh WHERE fh.member = :member")
    List<LayerForkHistory> findByMember(@Param("member") Member member);

    // 특정 원본 레이어의 모든 포크 이력 조회
    @Query("SELECT fh FROM LayerForkHistory fh WHERE fh.originalLayer = :originalLayer")
    List<LayerForkHistory> findByOriginalLayer(@Param("originalLayer") Layer originalLayer);

    // 포크된 레이어 ID들 조회 (특정 사용자가 특정 원본 레이어를 포크한 결과들)
    @Query("SELECT fh.forkedLayer.id FROM LayerForkHistory fh WHERE fh.originalLayer = :originalLayer AND fh.member = :member")
    List<Long> findForkedLayerIdsByOriginalLayerAndMember(@Param("originalLayer") Layer originalLayer, @Param("member") Member member);

} 