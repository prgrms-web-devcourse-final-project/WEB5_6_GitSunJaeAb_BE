package com.gitsunjaeab.mapick.domain.report;

import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findFirstByReporter(Member member);

    Report findFirstByReportedMember(Member member);

    Report findFirstByRoadmap(Roadmap roadmap);

    Report findFirstByMarker(Marker marker);

    Report findFirstByQuest(Quest quest);

    @Query("SELECT r FROM Report r "
        + "LEFT JOIN FETCH r.reporter "
        + "LEFT JOIN FETCH r.reportedMember "
        + "LEFT JOIN FETCH r.roadmap "
        + "LEFT JOIN FETCH r.marker "
        + "LEFT JOIN FETCH r.quest "
        + "WHERE r.id = :id")
    Optional<Report> findByIdWithFetch(@Param("id") Long id);
}
