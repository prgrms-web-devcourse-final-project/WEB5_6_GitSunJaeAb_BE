//package com.gitsunjaeab.mapick.api.admin;
//
//import com.gitsunjaeab.mapick.api.report.dto.ReportListResponse;
//import com.gitsunjaeab.mapick.application.report.ReportService;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
//public class AdminController {
//
//    private final ReportService reportService;
//
//    public AdminController(ReportService reportService) {
//        this.reportService = reportService;
//    }
//
//    // 전체 신고 조회 (관리자)
//    @GetMapping
//    public ResponseEntity<ReportListResponse> getAllReports() {
//        ReportListResponse response = reportService.findAll();
//
//        return ResponseEntity.ok(response);
//    }
//
//}
