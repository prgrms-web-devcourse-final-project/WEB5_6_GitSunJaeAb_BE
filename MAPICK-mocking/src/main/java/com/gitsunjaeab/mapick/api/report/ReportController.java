package com.gitsunjaeab.mapick.api.report;

import com.gitsunjaeab.mapick.api.report.dto.ReportDetailResponse;
import com.gitsunjaeab.mapick.api.report.dto.ReportListResponse;
import com.gitsunjaeab.mapick.api.report.dto.MapReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.QuestReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.MarkerReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.ReportProcessRequest;
import com.gitsunjaeab.mapick.application.report.ReportService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "신고 관리 API")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ===== 관리자용 API =====
    
    // 전체 신고 조회 (관리자)
    @GetMapping
    @Operation(summary = "전체 신고 조회", description = "[관리자용] 모든 신고 내역을 조회합니다.")
    public ResponseEntity<ReportListResponse> getAllReports() {
        ReportListResponse response = reportService.findAll();
        return ResponseEntity.ok(response);
    }

    // 특정 신고 상세 조회 (관리자)
    @GetMapping("/{reportId}")
    @Operation(summary = "특정 신고 상세 조회", description = "[관리자용] 특정 신고의 상세 정보를 조회합니다.")
    public ResponseEntity<ReportDetailResponse> getReport(@PathVariable(name = "reportId") final Long reportId) {
        ReportDetailResponse response = reportService.getReportDetail(reportId);
        return ResponseEntity.ok(response);
    }

    // 신고 처리 완료 (관리자)
    @PutMapping("/admin/{reportId}")
    @Operation(summary = "신고 처리 완료", description = "[관리자용] 신고 상태를 변경하여 처리 완료합니다.")
    public ResponseEntity<ApiResponse> processReport(@PathVariable(name = "reportId") final Long reportId,
            @RequestBody @Valid final ReportProcessRequest request) {
//        reportService.processReport(reportId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "신고 처리가 완료되었습니다."));
    }

    // ===== 사용자용 API (신고 생성) =====
    
    // 지도(로드맵) 신고 생성
    @PostMapping("/maps/{mapId}")
    @Operation(summary = "지도 신고 생성", description = "[사용자용] 특정 지도(로드맵)에 대한 신고를 접수합니다.")
    public ResponseEntity<ApiResponse> reportMap(@PathVariable(name = "mapId") final Long mapId,
            @RequestBody @Valid final MapReportRequest request) {
//        reportService.createMapReport(mapId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.OK, "지도 신고가 접수되었습니다."));
    }

    // 퀘스트 신고 생성
    @PostMapping("/quests/{questId}")
    @Operation(summary = "퀘스트 신고 생성", description = "[사용자용] 특정 퀘스트에 대한 신고를 접수합니다.")
    public ResponseEntity<ApiResponse> reportQuest(@PathVariable(name = "questId") final Long questId,
            @RequestBody @Valid final QuestReportRequest request) {
//        reportService.createQuestReport(questId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.OK, "퀘스트 신고가 접수되었습니다."));
    }

    // 마커 신고 생성
    @PostMapping("/markers/{markerId}")
    @Operation(summary = "마커 신고 생성", description = "[사용자용] 특정 마커에 대한 신고를 접수합니다.")
    public ResponseEntity<ApiResponse> reportMarker(@PathVariable(name = "markerId") final Long markerId,
            @RequestBody @Valid final MarkerReportRequest request) {
//        reportService.createMarkerReport(markerId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.OK, "마커 신고가 접수되었습니다."));
    }
}

