package com.gitsunjaeab.mapick.report;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.marker.entity.Marker;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import com.gitsunjaeab.mapick.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findFirstByReporter(Member member);

    Report findFirstByReportedMember(Member member);

    Report findFirstByMap(Map map);

    Report findFirstByMarker(Marker marker);

    Report findFirstByQuest(Quest quest);

}
