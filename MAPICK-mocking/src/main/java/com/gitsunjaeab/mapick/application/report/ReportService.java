package com.gitsunjaeab.mapick.application.report;

import com.gitsunjaeab.mapick.api.member.dto.MemberListResponse;
import com.gitsunjaeab.mapick.api.report.dto.ReportListResponse;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.api.report.dto.ReportDTO;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final QuestRepository questRepository;

    public ReportService(final ReportRepository reportRepository,
            final MemberRepository memberRepository, final RoadmapRepository roadmapRepository,
            final MarkerRepository markerRepository, final QuestRepository questRepository) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.markerRepository = markerRepository;
        this.questRepository = questRepository;
    }

    public ReportListResponse findAll() {
        final List<Report> reports = reportRepository.findAll(Sort.by("id"));
        return ReportListResponse.of(reports);
    }

    public ReportDTO get(final Long id) {
        return reportRepository.findById(id)
                .map(report -> roadmapToDTO(report, new ReportDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReportDTO reportDTO) {
        final Report report = new Report();
        roadmapToEntity(reportDTO, report);
        return reportRepository.save(report).getId();
    }

    public void update(final Long id, final ReportDTO reportDTO) {
        final Report report = reportRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(reportDTO, report);
        reportRepository.save(report);
    }

    public void delete(final Long id) {
        reportRepository.deleteById(id);
    }

    private ReportDTO roadmapToDTO(final Report report, final ReportDTO reportDTO) {
        reportDTO.setId(report.getId());
//        reportDTO.setReportType(report.getReportType());
        reportDTO.setDescription(report.getDescription());
        reportDTO.setStatus(report.getStatus());
        reportDTO.setCreatedAt(report.getCreatedAt());
        reportDTO.setResolvedAt(report.getResolvedAt());
        reportDTO.setReporter(report.getReporter() == null ? null : report.getReporter().getId());
        reportDTO.setReportedMember(report.getReportedMember() == null ? null : report.getReportedMember().getId());
        reportDTO.setRoadmap(report.getRoadmap() == null ? null : report.getRoadmap().getId());
        reportDTO.setMarker(report.getMarker() == null ? null : report.getMarker().getId());
        reportDTO.setQuest(report.getQuest() == null ? null : report.getQuest().getId());
        return reportDTO;
    }

    private Report roadmapToEntity(final ReportDTO reportDTO, final Report report) {
//        report.setReportType(reportDTO.getReportType());
        report.setDescription(reportDTO.getDescription());
        report.setStatus(reportDTO.getStatus());
        report.setCreatedAt(reportDTO.getCreatedAt());
        report.setResolvedAt(reportDTO.getResolvedAt());
        final Member reporter = reportDTO.getReporter() == null ? null : memberRepository.findById(reportDTO.getReporter())
                .orElseThrow(() -> new NotFoundException("reporter not found"));
        report.setReporter(reporter);
        final Member reportedMember = reportDTO.getReportedMember() == null ? null : memberRepository.findById(reportDTO.getReportedMember())
                .orElseThrow(() -> new NotFoundException("reportedMember not found"));
        report.setReportedMember(reportedMember);
        final Roadmap roadmap = reportDTO.getRoadmap() == null ? null : roadmapRepository.findById(reportDTO.getRoadmap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        report.setRoadmap(roadmap);
        final Marker marker = reportDTO.getMarker() == null ? null : markerRepository.findById(reportDTO.getMarker())
                .orElseThrow(() -> new NotFoundException("marker not found"));
        report.setMarker(marker);
        final Quest quest = reportDTO.getQuest() == null ? null : questRepository.findById(reportDTO.getQuest())
                .orElseThrow(() -> new NotFoundException("quest not found"));
        report.setQuest(quest);
        return report;
    }

}
