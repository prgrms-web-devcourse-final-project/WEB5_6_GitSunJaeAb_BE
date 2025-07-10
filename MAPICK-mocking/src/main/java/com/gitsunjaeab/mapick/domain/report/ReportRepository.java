package com.gitsunjaeab.mapick.domain.report;

import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findFirstByReporter(Member member);

    Report findFirstByReportedMember(Member member);

    Report findFirstByRoadmap(Roadmap roadmap);

    Report findFirstByMarker(Marker marker);

    Report findFirstByQuest(Quest quest);

}
