package com.gitsunjaeab.mapick.application.domain.report;

import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.quest.Quest;
import org.springframework.data.jpa.repository.JpaRepository;


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
