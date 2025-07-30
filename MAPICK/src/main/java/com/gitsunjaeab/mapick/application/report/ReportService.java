package com.gitsunjaeab.mapick.application.report;

import com.gitsunjaeab.mapick.api.report.dto.internal.ReportDTO;
import com.gitsunjaeab.mapick.api.report.dto.internal.ReportDetailDTO;
import com.gitsunjaeab.mapick.api.report.dto.internal.ReportSimpleDTO;
import com.gitsunjaeab.mapick.api.report.dto.request.MapReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.MarkerReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.QuestReportRequest;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.domain.report.ReportStatus;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final QuestRepository questRepository;

    // ===== 관리자용 메서드 =====

    // [관리자] 전체 신고 조회
    // complete
    @Transactional(readOnly = true)
    public List<ReportSimpleDTO> getAllReports() {

        final List<Report> reports = reportRepository.findAll(Sort.by("id"));

        List<ReportSimpleDTO> reportSimpleDTOS = reports.stream()
                .map(ReportSimpleDTO::of)
                .toList();

        return reportSimpleDTOS;
    }

    // [관리자] 특정 신고 조회
    // complete
    @Transactional(readOnly = true)
    public ReportDetailDTO getReportDetail(Long reportId) {

        final Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CommonException(ResponseCode.REPORT_NOT_FOUND));

        ReportDetailDTO reportDetailDTO = ReportDetailDTO.of(report);

        return reportDetailDTO;
    }

    // [관리자] 신고 완료 처리
    // complete
    @Transactional
    public void processReport(Long reportId) {

        final Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CommonException(ResponseCode.REPORT_NOT_FOUND));

        if(report.getStatus().equals(ReportStatus.RESOLVED)){
            throw new CommonException(ResponseCode.ALREADY_PROCESSED);
        }

        report.setStatus(ReportStatus.RESOLVED);
        report.setResolvedAt(OffsetDateTime.now());

    }

    // ===== 사용자용 신고 생성 메서드들 =====
    
    // [사용자] 지도(로드맵) 신고 생성
    // complete
    @Transactional
    public ReportDTO createMapReport(Long memberId, Long roadmapId, MapReportRequest mapReportRequest) {

        final Member reporter = memberRepository.findById(memberId)

                .orElseThrow(() -> new CommonException(ResponseCode.REPORTER_NOT_FOUND));

        final Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CommonException(ResponseCode.MAP_NOT_FOUND));

        if(reportRepository.existsByReporterAndRoadmap(reporter, roadmap)){
            throw new CommonException(ResponseCode.ALREADY_REPORTED);
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reportedMember(roadmap.getMember())
                .description(mapReportRequest.getDescription())
                .roadmap(roadmap)
                .status(ReportStatus.REPORTED)
                .createdAt(OffsetDateTime.now())
                .build();

        try {
            reportRepository.save(report);
        } catch (DataIntegrityViolationException e) {
            throw new CommonException(ResponseCode.SAVE_FAILED);
        }

        ReportDTO reportDTO = ReportDTO.of(report);

        return reportDTO;

    }

    // [사용자] 마커 신고 생성
    // complete
    @Transactional
    public ReportDTO createMarkerReport(Long memberId, Long markerId, MarkerReportRequest markerReportRequest) {

        final Member reporter = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.REPORTER_NOT_FOUND));

        final Marker marker = markerRepository.findById(markerId)
                .orElseThrow(() -> new CommonException(ResponseCode.MARKER_NOT_FOUND));


        if(reportRepository.existsByReporterAndMarker(reporter, marker)){
            throw new CommonException(ResponseCode.ALREADY_REPORTED);
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reportedMember(marker.getMember())
                .description(markerReportRequest.getDescription())
                .marker(marker)
                .status(ReportStatus.REPORTED)
                .createdAt(OffsetDateTime.now())
                .build();

        try {
            reportRepository.save(report);
        } catch (DataIntegrityViolationException e) {
            throw new CommonException(ResponseCode.SAVE_FAILED);
        }

        ReportDTO reportDTO = ReportDTO.of(report);

        return reportDTO;
    }
    
    // [사용자] 퀘스트 신고 생성
    // complete
    @Transactional
    public ReportDTO createQuestReport(Long memberId, Long questId, QuestReportRequest questReportRequest) {

        final Member reporter = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.REPORTER_NOT_FOUND));

        final Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new CommonException(ResponseCode.QUEST_NOT_FOUND));


        if(reportRepository.existsByReporterAndQuest(reporter, quest)){
            throw new CommonException(ResponseCode.ALREADY_REPORTED);
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reportedMember(quest.getMember())
                .description(questReportRequest.getDescription())
                .quest(quest)
                .status(ReportStatus.REPORTED)
                .createdAt(OffsetDateTime.now())
                .build();

        try {
            reportRepository.save(report);
        } catch (DataIntegrityViolationException e) {
            throw new CommonException(ResponseCode.SAVE_FAILED);
        }

        ReportDTO reportDTO = ReportDTO.of(report);

        return reportDTO;
    }


}
