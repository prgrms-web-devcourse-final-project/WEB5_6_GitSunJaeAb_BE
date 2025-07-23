package com.gitsunjaeab.mapick.api.report;

import com.gitsunjaeab.mapick.api.report.dto.*;
import com.gitsunjaeab.mapick.api.report.dto.request.MapReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.MarkerReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.request.QuestReportRequest;
import com.gitsunjaeab.mapick.api.report.dto.response.*;
import com.gitsunjaeab.mapick.application.report.ReportService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "신고 관리 API")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // ===== 관리자용 API =====
    
    // 전체 신고 조회 (관리자) -> todo 완성(예외처리 필요)
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자]전체 신고 조회", description = "[관리자용] 모든 신고 내역을 조회합니다.")
    public ResponseEntity<ReportListResponse> getAllReports() {

        List<ReportSimpleDTO> reportSimpleDTOS = reportService.findAll();

        ReportListResponse response = ReportListResponse.of(reportSimpleDTOS);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    // 특정 신고 상세 조회 (관리자) -> todo 완성(예외처리 필요)
    @GetMapping("/{reportId}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자]특정 신고 상세 조회", description = "[관리자용] 특정 신고의 상세 정보를 조회합니다.")
    public ResponseEntity<ReportResponse> getReport(@PathVariable(name = "reportId") final Long reportId) {

        ReportDetailDTO reportDetailDTO = reportService.getReportDetail(reportId);
        ReportResponse response = ReportResponse.of(reportDetailDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 특정 신고 처리 완료 (관리자) -> todo 완성(예외처리 필요)
    @GetMapping("/admin/{reportId}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자]신고 처리 완료", description = "[관리자용] 신고 상태를 변경하여 처리 완료합니다.")
    public ResponseEntity<ReportProcessResponse> processReport(@PathVariable(name = "reportId") final Long reportId) {

        reportService.processReport(reportId);
        ReportProcessResponse response = ReportProcessResponse.of();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // ===== 사용자용 API (신고 생성) =====
    
    // 지도(로드맵) 신고 생성 -> todo 완성(예외처리 필요)
    @PostMapping("/maps/{roadmapId}")
    @Operation(summary = "[사용자]지도 신고 생성", description = "[사용자용] 특정 지도(로드맵)에 대한 신고를 접수합니다.")
    public ResponseEntity<MapReportResponse> reportMap(@PathVariable(name = "roadmapId") final Long roadmapId,
            @RequestBody @Valid final MapReportRequest mapReportRequest) {

        ReportDTO reportDTO = reportService.createMapReport(roadmapId, mapReportRequest);
        MapReportResponse response = MapReportResponse.of(reportDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // 마커 신고 생성 -> todo org.hibernate.LazyInitializationException: 해결 필요
    @PostMapping("/markers/{markerId}")
    @Operation(summary = "[사용자]마커 신고 생성", description = "[사용자용] 특정 마커에 대한 신고를 접수합니다.")
    public ResponseEntity<MarkerReportResponse> reportMarker(@PathVariable(name = "markerId") final Long markerId,
                                                    @RequestBody @Valid final MarkerReportRequest markerReportRequest) {

        ReportDTO reportDTO = reportService.createMarkerReport(markerId, markerReportRequest);
        MarkerReportResponse response = MarkerReportResponse.of(reportDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 퀘스트 신고 생성 -> todo org.hibernate.LazyInitializationException: 해결 필요
    @PostMapping("/quests/{questId}")
    @Operation(summary = "[사용자]퀘스트 신고 생성", description = "[사용자용] 특정 퀘스트에 대한 신고를 접수합니다.")
    public ResponseEntity<QuestReportResponse> reportQuest(@PathVariable(name = "questId") final Long questId,
            @RequestBody @Valid final QuestReportRequest questReportRequest) {


        ReportDTO reportDTO = reportService.createQuestReport(questId, questReportRequest);
        QuestReportResponse response = QuestReportResponse.of(reportDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


}

