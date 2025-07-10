package com.gitsunjaeab.mapick.quest_rank;

import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.quest.Quest;
import com.gitsunjaeab.mapick.quest.QuestRepository;
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
@RequestMapping(value = "/api/questRanks", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestRankResource {

    private final QuestRankService questRankService;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;

    public QuestRankResource(final QuestRankService questRankService,
            final QuestRepository questRepository, final MemberRepository memberRepository) {
        this.questRankService = questRankService;
        this.questRepository = questRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<List<QuestRankDTO>> getAllQuestRanks() {
        return ResponseEntity.ok(questRankService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestRankDTO> getQuestRank(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(questRankService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createQuestRank(
            @RequestBody @Valid final QuestRankDTO questRankDTO) {
        final Long createdId = questRankService.create(questRankDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateQuestRank(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final QuestRankDTO questRankDTO) {
        questRankService.update(id, questRankDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuestRank(@PathVariable(name = "id") final Long id) {
        questRankService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/questValues")
    public ResponseEntity<Map<Long, String>> getQuestValues() {
        return ResponseEntity.ok(questRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Quest::getId, Quest::getTitle)));
    }

    @GetMapping("/memberValues")
    public ResponseEntity<Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

}
