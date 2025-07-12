package com.gitsunjaeab.mapick.api.report;

import com.gitsunjaeab.mapick.api.report.dto.ReportDetailResponse;
import com.gitsunjaeab.mapick.api.report.dto.ReportListResponse;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.application.report.ReportService;
import com.gitsunjaeab.mapick.api.report.dto.ReportDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 전체 신고 조회 (관리자)
    @GetMapping
    public ResponseEntity<ReportListResponse> getAllReports() {
        ReportListResponse response = reportService.findAll();

        return ResponseEntity.ok(response);
    }

    // 특정 신고 상세 조회 (관리자)
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailResponse> getReport(@PathVariable(name = "reportId") final Long roadmapId) {
        ReportDetailResponse response = reportService.getReportDetail(roadmapId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReport(@RequestBody @Valid final ReportDTO reportDTO) {
        final Long createdId = reportService.create(reportDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{reportsId}")
    public ResponseEntity<Long> updateReport(@PathVariable(name = "reportsId") final Long reportsId,
            @RequestBody @Valid final ReportDTO reportDTO) {
        reportService.update(reportsId, reportDTO);
        return ResponseEntity.ok(reportsId);
    }

    @DeleteMapping("/{reportsId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReport(@PathVariable(name = "reportsId") final Long reportsId) {
        reportService.delete(reportsId);
        return ResponseEntity.noContent().build();
    }

}

