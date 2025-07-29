package com.gitsunjaeab.mapick.api.report;

import com.gitsunjaeab.mapick.api.report.dto.ReportDTO;
import com.gitsunjaeab.mapick.api.report.dto.ReportDetailDTO;
import com.gitsunjaeab.mapick.api.report.dto.ReportSimpleDTO;
import com.gitsunjaeab.mapick.api.report.dto.request.MapReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.MarkerReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.QuestReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.response.*;
import com.gitsunjaeab.mapick.application.report.ReportService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "신고 관리 API")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    /**
    관리자
     */
    
    // [관리자] 전체 신고 조회
    // complete
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자]전체 신고 조회", description = "[관리자용] 모든 신고 내역을 조회합니다.")
    public ResponseEntity<ReportListResponse> getAllReports() {

        List<ReportSimpleDTO> reportSimpleDTOS = reportService.getAllReports();

        ReportListResponse response = ReportListResponse.of(reportSimpleDTOS);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    // [관리자]특정 신고 상세 조회
    // complete
    @GetMapping("/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자]특정 신고 상세 조회", description = "[관리자용] 특정 신고의 상세 정보를 조회합니다.")
    public ResponseEntity<ReportResponse> getReport(
            @PathVariable(name = "reportId") final Long reportId) {

        ReportDetailDTO reportDetailDTO = reportService.getReportDetail(reportId);
        ReportResponse response = ReportResponse.of(reportDetailDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // [관리자]특정 신고 처리 완료
    // complete
    @GetMapping("/admin/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자]신고 완료 처리", description = "[관리자용] 신고 상태를 변경하여 처리 완료합니다.")
    public ResponseEntity<ReportProcessResponse> processReport(
            @PathVariable(name = "reportId") final Long reportId) {

        reportService.processReport(reportId);

        ReportProcessResponse response = ReportProcessResponse.of();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * 사용자
     */

    // [사용자] 지도(로드맵) 신고 생성
    // complete
    @PostMapping("/maps/{roadmapId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "[사용자]지도 신고 생성", description = "[사용자용] 특정 지도(로드맵)에 대한 신고를 접수합니다.")
    public ResponseEntity<MapReportResponse> reportMap(
            @AuthenticationPrincipal Principal principal,
            @PathVariable(name = "roadmapId") final Long roadmapId,
            @RequestBody @Valid final MapReportRequest mapReportRequest) {

        Long memberId = principal.getMember().getId();

        ReportDTO reportDTO = reportService.createMapReport(memberId,roadmapId, mapReportRequest);


        MapReportResponse response = MapReportResponse.of(reportDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // [사용자] 마커 신고 생성
    // complete
    @PostMapping("/markers/{markerId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "[사용자]마커 신고 생성", description = "[사용자용] 특정 마커에 대한 신고를 접수합니다.")
    public ResponseEntity<MarkerReportResponse> reportMarker(
            @AuthenticationPrincipal Principal principal,
            @PathVariable(name = "markerId") final Long markerId,
            @RequestBody @Valid final MarkerReportRequest markerReportRequest) {

        Long memberId = principal.getMember().getId();

        ReportDTO reportDTO = reportService.createMarkerReport(memberId,markerId, markerReportRequest);


        MarkerReportResponse response = MarkerReportResponse.of(reportDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // [사용자] 퀘스트 신고 생성
    // complete
    @PostMapping("/quests/{questId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "[사용자]퀘스트 신고 생성", description = "[사용자용] 특정 퀘스트에 대한 신고를 접수합니다.")
    public ResponseEntity<QuestReportResponse> reportQuest(
            @AuthenticationPrincipal Principal principal,
            @PathVariable(name = "questId") final Long questId,
            @RequestBody @Valid final QuestReportRequest questReportRequest) {

        Long memberId = principal.getMember().getId();

        ReportDTO reportDTO = reportService.createQuestReport(memberId,questId, questReportRequest);


        QuestReportResponse response = QuestReportResponse.of(reportDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


}

