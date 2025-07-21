package com.gitsunjaeab.mapick.application.report;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestReportDTO;
import com.gitsunjaeab.mapick.api.report.dto.ReportDetailDTO;
import com.gitsunjaeab.mapick.api.report.dto.response.ReportResponse;
import com.gitsunjaeab.mapick.api.report.dto.request.MapReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.QuestReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.MarkerReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.ReportProcessRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerReportDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapReportDTO;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.domain.report.ReportStatus;
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

    // [관리자] 전체 신고 조회 // todo 정적 메서드로 넣기 , 빌더로 변경 하기
    @Transactional(readOnly = true)
    public List<ReportDTO> findAll() {

        final List<Report> reports = reportRepository.findAll(Sort.by("id"));


        List<ReportDTO> reportDTOS = reports.stream()
                .map(r -> new ReportDTO(
                        r.getId(),
                        new MemberSimpleDTO(r.getReporter()),
                        new MemberSimpleDTO(r.getReportedMember()),
                        r.getDescription(),
                        r.getRoadmap() != null ? r.getRoadmap().getId() : null,
                        r.getMarker() != null ? r.getMarker().getId() : null,
                        r.getQuest() != null ? r.getQuest().getId() : null,
                        r.getStatus(),
                        r.getCreatedAt(),
                        r.getResolvedAt()
                )
                ).toList();

        return reportDTOS;
    }

    // [관리자] 특정 신고 조회 // todo 정적 메서드로 넣기
    @Transactional(readOnly = true)
    public ReportDetailDTO getReportDetail(Long reportId) {

        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new EntityNotFoundException("해당 신고가 존재하지 않습니다. id=" + reportId));

        ReportDetailDTO reportDetailDTO = ReportDetailDTO.builder()
                .id(report.getId())
                .description(report.getDescription())
                .status(report.getStatus())
                .resolvedAt(report.getResolvedAt())
                .createdAt(report.getCreatedAt())
                .resolvedAt(report.getResolvedAt())
                .reporter(MemberDTO.of(report.getReporter()))
                .reportedMember(MemberDTO.of(report.getReportedMember()))
                .quest(report.getQuest() != null ? QuestReportDTO.of(report.getQuest()) : null)
                .marker(report.getMarker() != null ? MarkerReportDTO.of(report.getMarker()) : null)
                .roadmap(report.getRoadmap() != null ? RoadmapReportDTO.of(report.getRoadmap()) : null)
                .build();


        return reportDetailDTO;
    }


    // ===== 관리자용 메서드 =====
    
    // 신고 처리 완료 (관리자용)
    public void processReport(Long reportId, ReportProcessRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("신고를 찾을 수 없습니다"));
        
        report.setStatus(request.getStatus());
        
        // 처리 완료 시 해결 시간 설정
        if (request.getStatus() == ReportStatus.RESOLVED) {
            report.setResolvedAt(OffsetDateTime.now());
        }
        
        reportRepository.save(report);
    }

    // ===== 사용자용 신고 생성 메서드들 =====
    
    // 지도(로드맵) 신고 생성
    public void createMapReport(Long roadmapId, MapReportRequest mapReportRequest) {

        Member reporter = memberRepository.findById(mapReportRequest.getReporterId())
                .orElseThrow(() -> new NotFoundException("신고자를 찾을 수 없습니다"));
        
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new NotFoundException("지도를 찾을 수 없습니다"));
        
        Report report = new Report();

        report.setReporter(reporter);
        report.setDescription(mapReportRequest.getDescription());
        report.setRoadmap(roadmap);
        report.setStatus(ReportStatus.REPORTED);
        report.setCreatedAt(OffsetDateTime.now());
        
        reportRepository.save(report);
    }
    
    // 퀘스트 신고 생성
    public void createQuestReport(Long questId, QuestReportRequest request) {
        Member reporter = memberRepository.findById(request.getReporterId())
                .orElseThrow(() -> new NotFoundException("신고자를 찾을 수 없습니다"));
        
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NotFoundException("퀘스트를 찾을 수 없습니다"));
        
        Report report = new Report();
        report.setReporter(reporter);
        report.setDescription(request.getDescription());
        report.setQuest(quest);
        report.setStatus(ReportStatus.REPORTED);
        report.setCreatedAt(OffsetDateTime.now());
        
        reportRepository.save(report);
    }
    
    // 마커 신고 생성
    public void createMarkerReport(Long markerId, MarkerReportRequest request) {

        Member reporter = memberRepository.findById(request.getReporterId())
                .orElseThrow(() -> new NotFoundException("신고자를 찾을 수 없습니다"));
        
        Marker marker = markerRepository.findById(markerId)
                .orElseThrow(() -> new NotFoundException("마커를 찾을 수 없습니다"));
        
        Report report = new Report();
        report.setReporter(reporter);
        report.setDescription(request.getDescription());
        report.setMarker(marker);
        report.setStatus(ReportStatus.REPORTED);
        report.setCreatedAt(OffsetDateTime.now());
        
        reportRepository.save(report);
    }

    // ===== 기존 메서드들 (하위 호환성 유지) =====

    public Long create(final ReportDTO reportDTO) {
        final Report report = new Report();
        roadmapToEntity(reportDTO, report);
        // 생성 시간이 설정되지 않은 경우 현재 시간으로 설정
        if (report.getCreatedAt() == null) {
            report.setCreatedAt(OffsetDateTime.now());
        }
        return reportRepository.save(report).getId();
    }

    public void update(final Long id, final ReportDTO reportDTO) {
        final Report report = reportRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        
        // 기존 생성 시간 백업
        final OffsetDateTime originalCreatedAt = report.getCreatedAt();
        
        roadmapToEntity(reportDTO, report);
        
        // 생성 시간은 업데이트하지 않고 기존 값 유지
        report.setCreatedAt(originalCreatedAt);
        
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
        reportDTO.setReporter(new MemberSimpleDTO(report.getReporter()));
        reportDTO.setReportedMember(new MemberSimpleDTO(report.getReportedMember()));
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
        final Member reporter = reportDTO.getReporter() == null ? null : memberRepository.findById(reportDTO.getReporter().getId())
                .orElseThrow(() -> new NotFoundException("reporter not found"));
        report.setReporter(reporter);
        final Member reportedMember = reportDTO.getReportedMember() == null ? null : memberRepository.findById(reportDTO.getReportedMember().getId())
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
