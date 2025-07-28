package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MarkerRepository extends JpaRepository<Marker, Long> {

    @Query("SELECT m FROM Marker m WHERE m.member = :member AND m.deletedAt IS NULL")
    Marker findFirstByMember(@Param("member") Member member);

    @Query("SELECT m FROM Marker m WHERE m.layer = :layer AND m.deletedAt IS NULL")
    Marker findFirstByLayer(@Param("layer") Layer layer);

    @Query("SELECT m FROM Marker m WHERE m.layer.id = :layerId AND m.deletedAt IS NULL")
    List<Marker> findAllByLayer_Id(@Param("layerId") Long layerId);

    @Query("SELECT m FROM Marker m " +
            "JOIN FETCH m.layer l " +
            "JOIN FETCH l.roadmap " +
            "WHERE m.id = :id")
    Optional<Marker> findByIdWithLayerAndRoadmap(@Param("id") Long id);

}
