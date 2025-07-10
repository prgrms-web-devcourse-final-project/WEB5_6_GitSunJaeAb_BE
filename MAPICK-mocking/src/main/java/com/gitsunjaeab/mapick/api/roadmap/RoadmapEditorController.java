package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.application.roadmap.RoadmapEditorService;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapEditorDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
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

// NOTE 공유지도 참여자 (동시 수정자)를 어떻게 보이게 할지..정해지지 않아서 RoadmapController에 합치지 않음.
@RestController
@RequestMapping(value = "/roadmapEditors", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoadmapEditorController {

    private final RoadmapEditorService roadmapEditorService;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public RoadmapEditorController(final RoadmapEditorService roadmapEditorService,
            final RoadmapRepository roadmapRepository, final MemberRepository memberRepository) {
        this.roadmapEditorService = roadmapEditorService;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<List<RoadmapEditorDTO>> getAllRoadmapEditors() {
        return ResponseEntity.ok(roadmapEditorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoadmapEditorDTO> getRoadmapEditor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roadmapEditorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRoadmapEditor(
            @RequestBody @Valid final RoadmapEditorDTO mapEditorDTO) {
        final Long createdId = roadmapEditorService.create(mapEditorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoadmapEditor(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoadmapEditorDTO mapEditorDTO) {
        roadmapEditorService.update(id, mapEditorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoadmapEditor(@PathVariable(name = "id") final Long id) {
        roadmapEditorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mapValues")
    public ResponseEntity<Map<Long, String>> getRoadmapValues() {
        return ResponseEntity.ok(roadmapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedRoadmap(Roadmap::getId, Roadmap::getTitle)));
    }

    @GetMapping("/memberValues")
    public ResponseEntity<Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedRoadmap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/invitedByValues")
    public ResponseEntity<Map<Long, String>> getInvitedByValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedRoadmap(Member::getId, Member::getNickname)));
    }

}
