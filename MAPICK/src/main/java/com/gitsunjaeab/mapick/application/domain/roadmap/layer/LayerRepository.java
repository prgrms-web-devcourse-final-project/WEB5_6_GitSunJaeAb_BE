package com.gitsunjaeab.mapick.application.domain.roadmap.layer;

import com.gitsunjaeab.mapick.application.domain.member.Member;

import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LayerRepository extends JpaRepository<Layer, Long> {

    // ===== 기본 CRUD =====

    // 레이어 조회 - 모든 연관 엔티티 함께 조회 (LazyInitializationException 방지)
    @Query("SELECT l FROM Layer l JOIN FETCH l.member JOIN FETCH l.roadmap r JOIN FETCH r.member LEFT JOIN FETCH r.category WHERE l.roadmap.id = :roadmapId AND l.deletedAt IS NULL AND (l.isBlocked = false OR l.isBlocked IS NULL)")
    List<Layer> findAllByRoadmap_IdWithAssociations(@Param("roadmapId") Long roadmapId);



    // ===== 마이페이지 =====

    @Query("SELECT l FROM Layer l JOIN FETCH l.member JOIN FETCH l.roadmap r JOIN FETCH r.member LEFT JOIN FETCH r.category WHERE l.id = :id")
    Optional<Layer> findByIdWithAllAssociations(@Param("id") Long id);

    // 단일 레이어 조회 - 모든 연관 엔티티 함께 조회 (LazyInitializationException 방지)
    @Query("SELECT l FROM Layer l JOIN FETCH l.member JOIN FETCH l.roadmap r JOIN FETCH r.member LEFT JOIN FETCH r.category WHERE l.id = :id")
    java.util.Optional<Layer> findByIdWithMember(@Param("id") Long id);



    // ===== 다른 서비스에서 사용중인 메서드 =====
    @Query("SELECT l FROM Layer l WHERE l.member = :member AND l.deletedAt IS NULL AND (l.isBlocked = false OR l.isBlocked IS NULL)")
    Layer findFirstByMember(@Param("member") Member member);

    @Query("SELECT l FROM Layer l WHERE l.roadmap = :roadmap AND l.deletedAt IS NULL AND (l.isBlocked = false OR l.isBlocked IS NULL)")
    Layer findFirstByRoadmap(@Param("roadmap") Roadmap roadmap);

    @Query("SELECT l FROM Layer l WHERE l.roadmap = :roadmap AND l.deletedAt IS NULL")
    Layer findFirstNotDeletedByRoadmap(@Param("roadmap") Roadmap roadmap);

    @Query("""
        SELECT DISTINCT l FROM Layer l
        JOIN FETCH l.member
        JOIN FETCH l.roadmap r
        JOIN FETCH r.member
        LEFT JOIN FETCH r.category c
        LEFT JOIN FETCH l.layerMarkers
        WHERE l.id IN :ids
        AND l.deletedAt IS NULL
        AND (l.isBlocked = false OR l.isBlocked IS NULL)
        """)
    List<Layer> findByIdWithAllAssociations(@Param("ids") List<Long> ids);

    // ===== 미사용 =====

    // findAll - 모든 연관 엔티티 함께 조회 (LazyInitializationException 방지)
    @Query("SELECT l FROM Layer l JOIN FETCH l.member JOIN FETCH l.roadmap r JOIN FETCH r.member LEFT JOIN FETCH r.category WHERE l.deletedAt IS NULL AND (l.isBlocked = false OR l.isBlocked IS NULL)")
    List<Layer> findAllNotDeletedWithAssociations();

    // JPA 쿼리로 레이어 소프트 딜리트 (더 확실한 방법)
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Layer l SET l.deletedAt = CURRENT_TIMESTAMP WHERE l.id = :layerId")
    int softDeleteById(@Param("layerId") Long layerId);

    Optional<Layer> findByLayerTempId(Long layerTempId);

    @Query("SELECT l FROM Layer l WHERE l.roadmap.id = :roadmapId AND l.deletedAt IS NULL")
    List<Layer> findByRoadmapIdAndDeletedAtIsNull(@Param("roadmapId") Long roadmapId);

    @Query("SELECT l FROM Layer l " +
        "JOIN FETCH l.member " +
        "JOIN FETCH l.roadmap " +
        "WHERE l.id = :id")
    Optional<Layer> findByIdWithMemberAndRoadmap(@Param("id") Long id);
}
