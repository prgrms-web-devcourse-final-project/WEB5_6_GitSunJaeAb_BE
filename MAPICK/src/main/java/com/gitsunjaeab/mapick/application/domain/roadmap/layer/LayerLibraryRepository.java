package com.gitsunjaeab.mapick.application.domain.roadmap.layer;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LayerLibraryRepository extends JpaRepository<LayerLibrary, Long> {

    // ===== 마이페이지 =====

    // 찜 등록 - 모든 연관 엔티티 함께 조회 (LazyInitializationException 방지)
    @Query("SELECT ll FROM LayerLibrary ll JOIN FETCH ll.member JOIN FETCH ll.layer l JOIN FETCH l.member JOIN FETCH l.roadmap r JOIN FETCH r.member LEFT JOIN FETCH r.category WHERE ll.id = :id")
    Optional<LayerLibrary> findByIdWithAllAssociations(@Param("id") Long id);



    // 찜 조회 - 삭제되지 않은 것만 조회
    @Query("""
    SELECT ll.layer.id
    FROM LayerLibrary ll
    WHERE ll.member.id = :memberId
    AND ll.isZzim = true
    AND ll.deletedAt IS NULL
""")
    List<Long> findLayerIdsByMemberId(@Param("memberId") Long memberId);
    @Query("""
    SELECT DISTINCT l FROM Layer l
    JOIN FETCH l.member
    JOIN FETCH l.roadmap r
    JOIN FETCH r.member
    LEFT JOIN FETCH r.category
    LEFT JOIN FETCH l.layerMarkers m
    WHERE l.id IN :ids
    AND l.deletedAt IS NULL
    AND (l.isBlocked = false OR l.isBlocked IS NULL)
""")
    List<Layer> findByIdWithAllAssociations(@Param("ids") List<Long> ids);


    // 찜 등록 - 찜 중복 방지 (삭제되지 않은 것만 체크)
    @Query("SELECT COUNT(ll) > 0 FROM LayerLibrary ll WHERE ll.member = :member AND ll.layer = :layer AND ll.deletedAt IS NULL")
    boolean existsByMemberAndLayer(@Param("member") Member member, @Param("layer") Layer layer);


    // 찜 삭제 - 삭제되지 않은 것만 조회
    @Query("SELECT ll FROM LayerLibrary ll WHERE ll.member = :member AND ll.layer = :layer AND ll.deletedAt IS NULL")
    Optional<LayerLibrary> findByMemberAndLayer(@Param("member") Member member, @Param("layer") Layer layer);

    // 찜된 레이어 삭제하기 위한 검증 - 삭제되지 않은 것만 조회
    @Query("SELECT ll FROM LayerLibrary ll WHERE ll.layer.id = :layerId AND ll.isZzim = true AND ll.deletedAt IS NULL")
    List<LayerLibrary> findValidZzim(@Param("layerId") Long layerId);

    // Layer 기준으로 전체 찜 기록 소프트 딜리트 (새로 추가)
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE layer_libraries SET deleted_at = CURRENT_TIMESTAMP WHERE layer_id = :layerId AND deleted_at IS NULL", nativeQuery = true)
    int softDeleteAllByLayer(@Param("layerId") Long layerId);

    // Layer 기준으로 전체 찜 기록 삭제 (기존 하드 딜리트 - 필요시 사용)
    void deleteAllByLayer(Layer layer);


    Optional<LayerLibrary> findByMemberIdAndLayerId(Long memberId, Long layerId);
    List<LayerLibrary> findAllByMemberIdAndLayerId(Long memberId, Long layerId);
    boolean existsByMemberIdAndLayerId(Long memberId, Long layerId);

    @Query("""
    SELECT ll FROM LayerLibrary ll 
    WHERE ll.member = :member 
    AND ll.layer = :layer 
    AND ll.deletedAt IS NULL 
    AND ll.isZzim = true
""")
    Optional<LayerLibrary> findValidZzimByMemberAndLayer(@Param("member") Member member, @Param("layer") Layer layer);


    // ===== 다른 서비스에서 사용되는 메서드=====

    // 멤버 서비스 참조 무결성 검사 (삭제되지 않은 것만 체크)
    @Query("SELECT ll FROM LayerLibrary ll WHERE ll.member = :member AND ll.deletedAt IS NULL")
    LayerLibrary findFirstByMember(@Param("member") Member member);




    // ===== 추후 쓰일 것 같은 기능 =====

    // 인용수 조회 (로드맵서비스에서 사용) - 삭제되지 않은 것만 카운트
    @Query("""
        SELECT l.layer.roadmap.id AS roadmapId, COUNT(DISTINCT l.member.id) AS citationCount
        FROM LayerLibrary l
        WHERE l.layer.roadmap.id IN :roadmapIds AND l.deletedAt IS NULL
        GROUP BY l.layer.roadmap.id
    """)
    List<RoadmapCitationProjection> countDistinctMemberByRoadmapIds(@Param("roadmapIds") List<Long> roadmapIds);

    void deleteByMemberAndLayer(Member member, Layer layer);




    interface RoadmapCitationProjection {
        Long getRoadmapId();
        Long getCitationCount();
    }



    // ===== 안쓰이는데 만든 기능.... =====

    // 전체 회원 라이브러리 레이어 조회
//    @Query("SELECT DISTINCT ll.layer.id FROM LayerLibrary ll")
//    List<Long> findAllLayerIdsInLibrary();

}
