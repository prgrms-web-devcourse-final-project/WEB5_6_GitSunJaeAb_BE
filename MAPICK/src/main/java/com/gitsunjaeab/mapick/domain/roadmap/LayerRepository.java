package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LayerRepository extends JpaRepository<Layer, Long> {

    // ===== 기본 CRUD =====

    // 레이어 조회 (소프트 딜리트 조건 포함)
    @Query("SELECT l FROM Layer l WHERE l.roadmap.id = :roadmapId AND l.deletedAt IS NULL")
    List<Layer> findAllByRoadmap_Id(@Param("roadmapId") Long roadmapId);



    // ===== 마이페이지 =====

    // 찜 조회 - LazyInitializationException 방지 (삭제되지 않은 것만 조회)
    @Query("SELECT l FROM Layer l JOIN FETCH l.member WHERE l.id IN :ids AND l.deletedAt IS NULL")
    List<Layer> findAllByIdWithMember(@Param("ids") List<Long> ids);



    // ===== 다른 서비스에서 사용중인 메서드 =====
    @Query("SELECT l FROM Layer l WHERE l.member = :member AND l.deletedAt IS NULL")
    Layer findFirstByMember(@Param("member") Member member);

    @Query("SELECT l FROM Layer l WHERE l.roadmap = :roadmap AND l.deletedAt IS NULL")
    Layer findFirstByRoadmap(@Param("roadmap") Roadmap roadmap);




    // ===== 미사용 =====

    // 레이어 Id들로 해당 레이어들 조회 (사용자 찜 레이어 목록 조회)
    List<Layer> findAllByIdIn(List<Long> ids);

    // findAll (soft delete 제외하고 삭제되지 않은 레이어만 조회)
    @Query("SELECT l FROM Layer l WHERE l.deletedAt IS NULL")
    List<Layer> findAllNotDeleted();

    // 네이티브 쿼리로 소프트 딜리트 (더 확실한 방법)
    @Modifying
    @Query(value = "UPDATE Layers SET deleted_at = CURRENT_TIMESTAMP WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    int softDeleteById(@Param("id") Long id);

    // 로드맵 fetch join (필요할때 쓰려고 만들어놓음)
//    @Query("""
//    SELECT l FROM Layer l
//    JOIN FETCH l.member
//    LEFT JOIN FETCH l.roadmap
//    WHERE l.id IN :ids
//    """)
//    List<Layer> findAllByIdWithMemberAndRoadmap(@Param("ids") List<Long> ids);
//
//    // 찜할 때 안전하게 fetch join 후 반환하기
//    @Query("""
//            SELECT l FROM Layer l
//            JOIN FETCH l.member
//            WHERE l.id IN (
//                SELECT ll.layer.id FROM LayerLibrary ll WHERE ll.member.id = :memberId
//            )
//        """)
//    Layer findLibraryLayersByMemberWithMember(@Param("memberId") Long memberId);

}
