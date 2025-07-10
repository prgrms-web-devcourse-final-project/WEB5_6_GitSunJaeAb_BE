package com.gitsunjaeab.mapick.roadmap_editor;

import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.roadmap_editor.dto.RoadmapEditorDTO;
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
@RequestMapping(value = "/api/mapEditors", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoadmapEditorResource {

    private final RoadmapEditorService roadmapEditorService;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public RoadmapEditorResource(final RoadmapEditorService roadmapEditorService,
            final RoadmapRepository roadmapRepository, final MemberRepository memberRepository) {
        this.roadmapEditorService = roadmapEditorService;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<List<RoadmapEditorDTO>> getAllMapEditors() {
        return ResponseEntity.ok(roadmapEditorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoadmapEditorDTO> getMapEditor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roadmapEditorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMapEditor(
            @RequestBody @Valid final RoadmapEditorDTO roadmapEditorDTO) {
        final Long createdId = roadmapEditorService.create(roadmapEditorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMapEditor(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoadmapEditorDTO roadmapEditorDTO) {
        roadmapEditorService.update(id, roadmapEditorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMapEditor(@PathVariable(name = "id") final Long id) {
        roadmapEditorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mapValues")
    public ResponseEntity<Map<Long, String>> getMapValues() {
        return ResponseEntity.ok(roadmapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Roadmap::getId, Roadmap::getTitle)));
    }

    @GetMapping("/memberValues")
    public ResponseEntity<Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/invitedByValues")
    public ResponseEntity<Map<Long, String>> getInvitedByValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

}
