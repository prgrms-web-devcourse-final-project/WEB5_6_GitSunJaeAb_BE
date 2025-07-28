package com.gitsunjaeab.mapick.application.report;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.report.dto.ReportDTO;
import com.gitsunjaeab.mapick.api.report.dto.ReportDetailDTO;
import com.gitsunjaeab.mapick.api.report.dto.request.MapReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.QuestReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.MarkerReportRequest;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.domain.report.ReportStatus;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.api.report.dto.ReportSimpleDTO;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.util.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @Transactional(readOnly = true)
    public List<ReportSimpleDTO> getAllReports() {

        final List<Report> reports = reportRepository.findAll(Sort.by("id"));

        List<ReportSimpleDTO> reportSimpleDTOS = reports.stream()
                .map(ReportSimpleDTO::of)
                .toList();

        return reportSimpleDTOS;
    }

    // [관리자] 특정 신고 조회
    @Transactional(readOnly = true)
    public ReportDetailDTO getReportDetail(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("해당 신고가 존재하지 않습니다. id=" + reportId));

        ReportDetailDTO reportDetailDTO = ReportDetailDTO.of(report);

        return reportDetailDTO;
    }

    // 신고 처리 완료 (관리자용)
    @Transactional
    public void processReport(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("신고를 찾을 수 없습니다"));
        
        report.setStatus(ReportStatus.RESOLVED);
        report.setResolvedAt(OffsetDateTime.now());
        
        reportRepository.save(report);
    }

    // ===== 사용자용 신고 생성 메서드들 =====
    
    // 지도(로드맵) 신고 생성
    @Transactional
    public ReportDTO createMapReport(Long roadmapId, MapReportRequest mapReportRequest) {

        Member reporter = memberRepository.findById(mapReportRequest.getReporterId())
                .orElseThrow(() -> new NotFoundException("신고자를 찾을 수 없습니다"));
        
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new NotFoundException("지도를 찾을 수 없습니다"));
        
        Report report = Report.builder()
                .reporter(reporter)
                .reportedMember(roadmap.getMember())
                .description(mapReportRequest.getDescription())
                .roadmap(roadmap)
                .status(ReportStatus.REPORTED)
                .createdAt(OffsetDateTime.now())
                .build();
        
        reportRepository.save(report);

        ReportDTO reportDTO = ReportDTO.of(report);

        return reportDTO;

    }

    // 마커 신고 생성 -> todo org.hibernate.LazyInitializationException: 해결 필요
    @Transactional
    public ReportDTO createMarkerReport(Long markerId, MarkerReportRequest markerReportRequest) {

        Member reporter = memberRepository.findById(markerReportRequest.getReporterId())
                .orElseThrow(() -> new NotFoundException("신고자를 찾을 수 없습니다"));

        Marker marker = markerRepository.findById(markerId)
                .orElseThrow(() -> new NotFoundException("마커를 찾을 수 없습니다"));

        Report report = Report.builder()
                .reporter(reporter)
                .reportedMember(marker.getMember())
                .description(markerReportRequest.getDescription())
                .marker(marker)
                .status(ReportStatus.REPORTED)
                .createdAt(OffsetDateTime.now())
                .build();

        reportRepository.save(report);

        ReportDTO reportDTO = ReportDTO.of(report);

        return reportDTO;
    }
    
    // 퀘스트 신고 생성 -> todo org.hibernate.LazyInitializationException: 해결 필요
    @Transactional
    public ReportDTO createQuestReport(Long questId, QuestReportRequest questReportRequest) {

        Member reporter = memberRepository.findById(questReportRequest.getReporterId())
                .orElseThrow(() -> new NotFoundException("신고자를 찾을 수 없습니다"));
        
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NotFoundException("퀘스트를 찾을 수 없습니다"));

        Report report = Report.builder()
                .reporter(reporter)
                .reportedMember(quest.getMember())
                .description(questReportRequest.getDescription())
                .quest(quest)
                .status(ReportStatus.REPORTED)
                .createdAt(OffsetDateTime.now())
                .build();

        reportRepository.save(report);

        ReportDTO reportDTO = ReportDTO.of(report);

        return reportDTO;
    }


}
