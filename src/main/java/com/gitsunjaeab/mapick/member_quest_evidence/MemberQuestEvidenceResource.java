package com.gitsunjaeab.mapick.member_quest_evidence;

import com.gitsunjaeab.mapick.member_quest.MemberQuest;
import com.gitsunjaeab.mapick.member_quest.MemberQuestRepository;
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
@RequestMapping(value = "/api/memberQuestEvidences", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberQuestEvidenceResource {

    private final MemberQuestEvidenceService memberQuestEvidenceService;
    private final MemberQuestRepository memberQuestRepository;

    public MemberQuestEvidenceResource(final MemberQuestEvidenceService memberQuestEvidenceService,
            final MemberQuestRepository memberQuestRepository) {
        this.memberQuestEvidenceService = memberQuestEvidenceService;
        this.memberQuestRepository = memberQuestRepository;
    }

    @GetMapping
    public ResponseEntity<List<MemberQuestEvidenceDTO>> getAllMemberQuestEvidences() {
        return ResponseEntity.ok(memberQuestEvidenceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberQuestEvidenceDTO> getMemberQuestEvidence(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(memberQuestEvidenceService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberQuestEvidence(
            @RequestBody @Valid final MemberQuestEvidenceDTO memberQuestEvidenceDTO) {
        final Long createdId = memberQuestEvidenceService.create(memberQuestEvidenceDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberQuestEvidence(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MemberQuestEvidenceDTO memberQuestEvidenceDTO) {
        memberQuestEvidenceService.update(id, memberQuestEvidenceDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberQuestEvidence(
            @PathVariable(name = "id") final Long id) {
        memberQuestEvidenceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memberQuestValues")
    public ResponseEntity<Map<Long, String>> getMemberQuestValues() {
        return ResponseEntity.ok(memberQuestRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(MemberQuest::getId, MemberQuest::getStatus)));
    }

}
