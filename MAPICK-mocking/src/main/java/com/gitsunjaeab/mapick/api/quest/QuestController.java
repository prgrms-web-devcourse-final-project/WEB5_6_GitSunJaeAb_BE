package com.gitsunjaeab.mapick.api.quest;

import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.application.quest.MemberQuestService;
import com.gitsunjaeab.mapick.application.quest.QuestRankService;
import com.gitsunjaeab.mapick.application.quest.QuestService;
import com.gitsunjaeab.mapick.application.quest.MemberQuestEvidenceService;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/quests", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestController {

    private final QuestService questService;
    private final QuestRankService questRankService;
    private final MemberQuestService memberQuestService;
    private final MemberQuestEvidenceService memberQuestEvidenceService;

    public QuestController(final QuestService questService, final QuestRankService questRankService,
        final MemberQuestService memberQuestService,
        final MemberQuestEvidenceService memberQuestEvidenceService) {
        this.questService = questService;
        this.questRankService = questRankService;
        this.memberQuestService = memberQuestService;
        this.memberQuestEvidenceService = memberQuestEvidenceService;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createQuest(@RequestBody @Valid final QuestDTO questDTO) {
        final Long createdId = questService.create(questDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    // 전체 퀘스트 조회
    @GetMapping
    public ResponseEntity<List<QuestDTO>> getAllQuests() {
        return ResponseEntity.ok(questService.findAll());
    }

    @GetMapping("/{questsId}")
    public ResponseEntity<QuestDTO> getQuest(@PathVariable(name = "questsId") final Long questsId) {
        return ResponseEntity.ok(questService.get(questsId));
    }

    @PutMapping("/{questsId}")
    public ResponseEntity<Long> updateQuest(@PathVariable(name = "questsId") final Long questsId,
        @RequestBody @Valid final QuestDTO questDTO) {
        questService.update(questsId, questDTO);
        return ResponseEntity.ok(questsId);
    }

    @DeleteMapping("/{questsId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuest(@PathVariable(name = "questsId") final Long questsId) {
        final ReferencedWarning referencedWarning = questService.getReferencedWarning(questsId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        questService.delete(questsId);
        return ResponseEntity.noContent().build();
    }

    // TODO Quest 정답 순서 >> QuestRank GET
//    @GetMapping("/ ")
//    public ResponseEntity<List<QuestRankDTO>> getAllQuestRanks() {
//        return ResponseEntity.ok(questRankService.findAll());
//    }

    // TODO MemberQuest >> 참여 누른 사람들 GET (전체 참여자 조회)
//    @GetMapping("/ ")
//    public ResponseEntity<List<MemberQuestDTO>> getAllMemberQuests() {
//        return ResponseEntity.ok(memberQuestService.findAll());
//    }

    // TODO 퀘스트 참여하기 (증거제출)
    // TODO 1. MemberQuest >> 참여자 POST (테이블에 참여자 명단 들어가고)
    // TODO 2. MemberQuestEvidence >> 참여하기 POST (테이블에 참여자가 제출한 증거자료 들어감)
//    @PostMapping("/ ")
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createMemberQuest(
//        @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
//        final Long createdId = memberQuestService.create(memberQuestDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }

    // TODO 참여자 답안 조회 (전체, 정답, 오답)
//    @GetMapping("/ ")
//    public ResponseEntity<List<MemberQuestEvidenceDTO>> getAllMemberQuestEvidences() {
//        return ResponseEntity.ok(memberQuestEvidenceService.findAll());
//    }

}
