package com.gitsunjaeab.mapick.member_quest;

import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.quest.Quest;
import com.gitsunjaeab.mapick.quest.QuestRepository;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
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
@RequestMapping(value = "/api/memberQuests", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberQuestResource {

    private final MemberQuestService memberQuestService;
    private final MemberRepository memberRepository;
    private final QuestRepository questRepository;

    public MemberQuestResource(final MemberQuestService memberQuestService,
            final MemberRepository memberRepository, final QuestRepository questRepository) {
        this.memberQuestService = memberQuestService;
        this.memberRepository = memberRepository;
        this.questRepository = questRepository;
    }

    @GetMapping
    public ResponseEntity<List<MemberQuestDTO>> getAllMemberQuests() {
        return ResponseEntity.ok(memberQuestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberQuestDTO> getMemberQuest(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(memberQuestService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberQuest(
            @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
        final Long createdId = memberQuestService.create(memberQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberQuest(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
        memberQuestService.update(id, memberQuestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberQuest(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = memberQuestService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        memberQuestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memberValues")
    public ResponseEntity<Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/questValues")
    public ResponseEntity<Map<Long, String>> getQuestValues() {
        return ResponseEntity.ok(questRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Quest::getId, Quest::getTitle)));
    }

}
