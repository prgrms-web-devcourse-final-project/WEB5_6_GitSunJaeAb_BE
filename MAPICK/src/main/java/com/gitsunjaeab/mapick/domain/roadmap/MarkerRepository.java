package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface MarkerRepository extends JpaRepository<Marker, Long> {

    @Query("SELECT m FROM Marker m WHERE m.member = :member AND m.deletedAt IS NULL")
    Marker findFirstByMember(@Param("member") Member member);

    @Query("SELECT m FROM Marker m WHERE m.layer = :layer AND m.deletedAt IS NULL")
    Marker findFirstByLayer(@Param("layer") Layer layer);

    @Query("SELECT m FROM Marker m WHERE m.layer.id = :layerId")
    List<Marker> findAllByLayer_Id(@Param("layerId") Long layerId);

    @Query("SELECT m FROM Marker m " +
            "JOIN FETCH m.layer l " +
            "JOIN FETCH l.roadmap " +
            "WHERE m.id = :id")
    Optional<Marker> findByIdWithLayerAndRoadmap(@Param("id") Long id);

    Optional<Marker> findByMarkerTempId(Long markerTempId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Marker m WHERE m.layer.id = :layerId")
    void deleteByLayerId(@Param("layerId") Long layerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Marker m WHERE m.layer.id = :layerId")
    void deleteAllByLayerId(@Param("layerId") Long layerId);
}
