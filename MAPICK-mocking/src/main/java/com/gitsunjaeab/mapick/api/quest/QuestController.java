package com.gitsunjaeab.mapick.api.quest;

import com.gitsunjaeab.mapick.api.quest.dto.QuestResponse;
import com.gitsunjaeab.mapick.application.quest.MemberQuestService;
import com.gitsunjaeab.mapick.application.quest.QuestRankService;
import com.gitsunjaeab.mapick.application.quest.QuestService;
import com.gitsunjaeab.mapick.application.quest.MemberQuestEvidenceService;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.api.quest.dto.QuestListResponse;
import jakarta.validation.Valid;
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
import com.gitsunjaeab.mapick.api.quest.dto.QuestRequest;


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

    // 퀘스트 생성
    @PostMapping
    public ResponseEntity<ApiResponse> createQuest(@RequestBody @Valid final QuestRequest questRequest) {
        questService.create(questRequest);  // 서비스도 Request 받도록 수정 필요
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 생성 완료"));
    }

    // 전체 퀘스트 조회
    @GetMapping
    public ResponseEntity<QuestListResponse> getAllQuests() {
        return ResponseEntity.ok(QuestListResponse.of(questService.findAll()));
    }

    // 단일 퀘스트 조회
    @GetMapping("/{questsId}")
    public ResponseEntity<QuestResponse> getQuest(@PathVariable(name = "questsId") final Long questsId) {
        return ResponseEntity.ok(questService.get(questsId));
    }

    // 퀘스트 수정
    @PutMapping("/{questsId}")
    public ResponseEntity<ApiResponse> updateQuest(@PathVariable(name = "questsId") final Long questsId,
        @RequestBody @Valid final QuestRequest questRequest) {
        questService.update(questsId, questRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 수정 완료"));
    }

    // 퀘스트 삭제
    @DeleteMapping("/{questsId}")
    public ResponseEntity<ApiResponse> deleteQuest(@PathVariable(name = "questsId") final Long questsId) {
        final ReferencedWarning referencedWarning = questService.getReferencedWarning(questsId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        questService.delete(questsId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 삭제 완료"));
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
