package com.gitsunjaeab.mapick.api.report;

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
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final QuestRepository questRepository;

    public ReportController(final ReportService reportService,
            final MemberRepository memberRepository, final RoadmapRepository roadmapRepository,
            final MarkerRepository markerRepository, final QuestRepository questRepository) {
        this.reportService = reportService;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.markerRepository = markerRepository;
        this.questRepository = questRepository;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReport(@RequestBody @Valid final ReportDTO reportDTO) {
        final Long createdId = reportService.create(reportDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<List<ReportDTO>> getAllReports() {
//        return ResponseEntity.ok(reportService.findAll());
//    }

//    @GetMapping("/{reportsId}")
//    public ResponseEntity<ReportDTO> getReport(@PathVariable(name = "reportsId") final Long reportsId) {
//        return ResponseEntity.ok(reportService.get(reportsId));
//    }

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

//    @GetMapping("/reporterValues")
//    public ResponseEntity<Map<Long, String>> getReporterValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//    @GetMapping("/reportedMemberValues")
//    public ResponseEntity<Map<Long, String>> getReportedMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//    @GetMapping("/mapValues")
//    public ResponseEntity<Map<Long, String>> getMapValues() {
//        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.Map::getId, com.gitsunjaeab.mapick.map.Map::getTitle)));
//    }
//
//    @GetMapping("/markerValues")
//    public ResponseEntity<Map<Long, Long>> getMarkerValues() {
//        return ResponseEntity.ok(markerRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Marker::getId, Marker::getId)));
//    }
//
//    @GetMapping("/questValues")
//    public ResponseEntity<Map<Long, String>> getQuestValues() {
//        return ResponseEntity.ok(questRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Quest::getId, Quest::getTitle)));
//    }

}
