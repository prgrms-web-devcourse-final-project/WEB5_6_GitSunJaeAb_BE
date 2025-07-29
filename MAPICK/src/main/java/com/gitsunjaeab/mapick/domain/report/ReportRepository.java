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

    boolean existsByRoadmapId(Long roadmapId);

    // 신고자와 로드맵 조합으로 중복 신고 확인
    boolean existsByReporterAndRoadmap(Member reporter, Roadmap roadmap);

    // 신고자와 마커 조합으로 중복 신고 확인
    boolean existsByReporterAndMarker(Member reporter, Marker marker);

    // 신고자와 퀘스트 조합으로 중복 신고 확인
    boolean existsByReporterAndQuest(Member reporter, Quest quest);



}
