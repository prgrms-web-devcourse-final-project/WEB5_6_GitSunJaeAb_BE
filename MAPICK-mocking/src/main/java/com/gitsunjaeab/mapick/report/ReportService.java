package com.gitsunjaeab.mapick.report;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.marker.Marker;
import com.gitsunjaeab.mapick.marker.MarkerRepository;
import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.quest.Quest;
import com.gitsunjaeab.mapick.quest.QuestRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final MapRepository mapRepository;
    private final MarkerRepository markerRepository;
    private final QuestRepository questRepository;

    public ReportService(final ReportRepository reportRepository,
            final MemberRepository memberRepository, final MapRepository mapRepository,
            final MarkerRepository markerRepository, final QuestRepository questRepository) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.mapRepository = mapRepository;
        this.markerRepository = markerRepository;
        this.questRepository = questRepository;
    }

    public List<ReportDTO> findAll() {
        final List<Report> reports = reportRepository.findAll(Sort.by("id"));
        return reports.stream()
                .map(report -> mapToDTO(report, new ReportDTO()))
                .toList();
    }

    public ReportDTO get(final Long id) {
        return reportRepository.findById(id)
                .map(report -> mapToDTO(report, new ReportDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReportDTO reportDTO) {
        final Report report = new Report();
        mapToEntity(reportDTO, report);
        return reportRepository.save(report).getId();
    }

    public void update(final Long id, final ReportDTO reportDTO) {
        final Report report = reportRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reportDTO, report);
        reportRepository.save(report);
    }

    public void delete(final Long id) {
        reportRepository.deleteById(id);
    }

    private ReportDTO mapToDTO(final Report report, final ReportDTO reportDTO) {
        reportDTO.setId(report.getId());
        reportDTO.setReportType(report.getReportType());
        reportDTO.setDescription(report.getDescription());
        reportDTO.setStatus(report.getStatus());
        reportDTO.setCreatedAt(report.getCreatedAt());
        reportDTO.setResolvedAt(report.getResolvedAt());
        reportDTO.setReporter(report.getReporter() == null ? null : report.getReporter().getId());
        reportDTO.setReportedMember(report.getReportedMember() == null ? null : report.getReportedMember().getId());
        reportDTO.setMap(report.getMap() == null ? null : report.getMap().getId());
        reportDTO.setMarker(report.getMarker() == null ? null : report.getMarker().getId());
        reportDTO.setQuest(report.getQuest() == null ? null : report.getQuest().getId());
        return reportDTO;
    }

    private Report mapToEntity(final ReportDTO reportDTO, final Report report) {
        report.setReportType(reportDTO.getReportType());
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
        final Map map = reportDTO.getMap() == null ? null : mapRepository.findById(reportDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        report.setMap(map);
        final Marker marker = reportDTO.getMarker() == null ? null : markerRepository.findById(reportDTO.getMarker())
                .orElseThrow(() -> new NotFoundException("marker not found"));
        report.setMarker(marker);
        final Quest quest = reportDTO.getQuest() == null ? null : questRepository.findById(reportDTO.getQuest())
                .orElseThrow(() -> new NotFoundException("quest not found"));
        report.setQuest(quest);
        return report;
    }

}
