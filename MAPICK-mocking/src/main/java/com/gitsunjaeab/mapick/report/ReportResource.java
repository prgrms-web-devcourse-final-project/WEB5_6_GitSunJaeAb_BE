package com.gitsunjaeab.mapick.report;

import com.gitsunjaeab.mapick.report.dto.ReportDTO;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.marker.entity.Marker;
import com.gitsunjaeab.mapick.marker.MarkerRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import com.gitsunjaeab.mapick.quest.QuestRepository;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
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
@RequestMapping(value = "/api/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportResource {

    private final ReportService reportService;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final QuestRepository questRepository;

    public ReportResource(final ReportService reportService,
            final MemberRepository memberRepository, final RoadmapRepository roadmapRepository,
            final MarkerRepository markerRepository, final QuestRepository questRepository) {
        this.reportService = reportService;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.markerRepository = markerRepository;
        this.questRepository = questRepository;
    }

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(reportService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReport(@RequestBody @Valid final ReportDTO reportDTO) {
        final Long createdId = reportService.create(reportDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReport(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ReportDTO reportDTO) {
        reportService.update(id, reportDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReport(@PathVariable(name = "id") final Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reporterValues")
    public ResponseEntity<Map<Long, String>> getReporterValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/reportedMemberValues")
    public ResponseEntity<Map<Long, String>> getReportedMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/mapValues")
    public ResponseEntity<Map<Long, String>> getMapValues() {
        return ResponseEntity.ok(roadmapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Roadmap::getId, Roadmap::getTitle)));
    }

    @GetMapping("/markerValues")
    public ResponseEntity<Map<Long, Long>> getMarkerValues() {
        return ResponseEntity.ok(markerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Marker::getId, Marker::getId)));
    }

    @GetMapping("/questValues")
    public ResponseEntity<Map<Long, String>> getQuestValues() {
        return ResponseEntity.ok(questRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Quest::getId, Quest::getTitle)));
    }

}
