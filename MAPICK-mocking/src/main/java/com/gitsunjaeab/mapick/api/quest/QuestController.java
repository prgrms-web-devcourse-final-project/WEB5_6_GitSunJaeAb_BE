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
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRankResponse;
import java.util.List;


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

    // 퀘스트 랭킹 조회 (특정 퀘스트의 랭킹)
    @GetMapping("/{questId}/rankings")
    public ResponseEntity<List<QuestRankResponse>> getQuestRankings(@PathVariable(name = "questId") final Long questId) {
        return ResponseEntity.ok(questRankService.findByQuestId(questId));
    }

    // 퀘스트 참여자 목록 조회 (특정 퀘스트의 참여자들)
    @GetMapping("/{questId}/memberQuest")
    public ResponseEntity<List<MemberQuestResponse>> getQuestParticipants(@PathVariable(name = "questId") final Long questId) {
        return ResponseEntity.ok(memberQuestService.findByQuestId(questId));
    }

    // 퀘스트 참여 신청
    @PostMapping("/{questId}/memberQuest")
    public ResponseEntity<ApiResponse> participateInQuest(@PathVariable(name = "questId") final Long questId,
            @RequestBody @Valid final MemberQuestRequest memberQuestRequest) {
        // questId를 request에 설정
        memberQuestRequest.setQuest(questId);
        memberQuestService.create(memberQuestRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 참여 신청 완료"));
    }

    // 증빙 자료 제출
    @PostMapping("/memberQuest/{memberQuestId}/evidence")
    public ResponseEntity<ApiResponse> submitEvidence(@PathVariable(name = "memberQuestId") final Long memberQuestId,
            @RequestBody @Valid final MemberQuestEvidenceRequest evidenceRequest) {
        // memberQuestId를 request에 설정
        evidenceRequest.setMemberQuest(memberQuestId);
        memberQuestEvidenceService.create(evidenceRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "증빙 자료 제출 완료"));
    }

    // 특정 참여자의 증빙 자료 조회
    @GetMapping("/memberQuest/{memberQuestId}/evidence")
    public ResponseEntity<List<MemberQuestEvidenceResponse>> getParticipantEvidence(@PathVariable(name = "memberQuestId") final Long memberQuestId) {
        return ResponseEntity.ok(memberQuestEvidenceService.findByMemberQuestId(memberQuestId));
    }

    // 전체 참여자 답안 조회 (관리자용)
    @GetMapping("/evidence")
    public ResponseEntity<List<MemberQuestEvidenceResponse>> getAllEvidence() {
        return ResponseEntity.ok(memberQuestEvidenceService.findAll());
    }

    // 증빙 자료 삭제
    @DeleteMapping("/evidence/{evidenceId}")
    public ResponseEntity<ApiResponse> deleteEvidence(@PathVariable(name = "evidenceId") final Long evidenceId) {
        memberQuestEvidenceService.delete(evidenceId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "증빙 자료 삭제 완료"));
    }

}
