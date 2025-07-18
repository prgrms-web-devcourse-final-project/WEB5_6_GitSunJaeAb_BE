package com.gitsunjaeab.mapick.api.quest;

import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestListResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestDetailResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestListResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.application.quest.MemberQuestService;
import com.gitsunjaeab.mapick.application.quest.QuestRankService;
import com.gitsunjaeab.mapick.application.quest.QuestService;
import com.gitsunjaeab.mapick.application.quest.MemberQuestEvidenceService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRankResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;

@RestController
@RequestMapping(value = "/quests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "퀘스트 관리", description = "퀘스트 관련 API (출제자용/참여자용)")
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

    // ===== 출제자용 API (퀘스트 관리) =====
    
    // 퀘스트 생성 (출제자용)
    @PostMapping
    @Operation(summary = "퀘스트 생성", description = "[출제자용] 새로운 퀘스트를 생성합니다. 본인만 접근 가능합니다.")
    public ResponseEntity<ApiResponse> createQuest(
        @RequestBody @Valid final QuestRequest questRequest,
        @AuthenticationPrincipal Principal principal) {

        if (principal == null){
            throw new IllegalStateException("인증된 유저 정보 없음");
        }

        Long memberId = principal.getMember().getId();
        Long questId = questService.create(questRequest);
        QuestResponse createdQuest = questService.get(questId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 생성 완료"));
    }

    // 퀘스트 수정 (출제자용)
    @PutMapping("/{questsId}")
    @Operation(summary = "퀘스트 수정", description = "[출제자용] 본인이 생성한 퀘스트의 정보를 수정합니다.")
    public ResponseEntity<ApiResponse> updateQuest(@PathVariable(name = "questsId") final Long questsId,
            @RequestBody @Valid final QuestRequest questRequest) {
//        questService.update(questsId, questRequest);
//        QuestResponse updatedQuest = questService.get(questsId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 수정 완료"));
    }

    // 퀘스트 삭제 (출제자용)
    @DeleteMapping("/{questsId}")
    @Operation(summary = "퀘스트 삭제", description = "[출제자용] 본인이 생성한 퀘스트를 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteQuest(@PathVariable(name = "questsId") final Long questsId) {
//        final ReferencedWarning referencedWarning = questService.getReferencedWarning(questsId);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        questService.delete(questsId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 삭제 완료"));
    }

    // 본인 퀘스트 증빙 자료 조회 (출제자용 - 정답 처리시 사용)
    @GetMapping("/evidence")
    @Operation(summary = "본인 퀘스트 증빙 자료 조회", description = "[출제자용] (추후 추가예정) 본인이 생성한 퀘스트의 모든 증빙 자료를 조회합니다. 정답 처리시 사용됩니다.")
    public ResponseEntity<List<MemberQuestEvidenceResponse>> getAllEvidence() {
        return ResponseEntity.ok(memberQuestEvidenceService.findAll());
    }

    // ===== 참여자용 API (퀘스트 참여 및 조회) =====
    
    // 전체 퀘스트 조회 (참여자용)
    @GetMapping
    @Operation(summary = "전체 퀘스트 조회", description = "[참여자용] 활성화된 퀘스트 목록을 조회합니다.")
    public ResponseEntity<QuestListResponse> getAllQuests(
        @RequestParam(required = false) Boolean isActive ) {

        return ResponseEntity.ok(QuestListResponse.of(questService.findAll(isActive)));
    }

    // 특정 퀘스트 조회 (참여자용)
    @GetMapping("/{questsId}")
    @Operation(summary = "퀘스트 상세 조회", description = "[참여자용] 특정 퀘스트의 상세 정보를 조회합니다.")
    public ResponseEntity<QuestDetailResponse> getQuest(@PathVariable(name = "questsId") final Long questsId) {
        QuestResponse questResponse = questService.get(questsId);
        return ResponseEntity.ok(QuestDetailResponse.of(questResponse));
    }

    // 퀘스트 랭킹 조회 (참여자용)
    @GetMapping("/{questId}/rankings")
    @Operation(summary = "퀘스트 랭킹 조회", description = "[참여자용] (추후 추가예정) 특정 퀘스트의 랭킹을 조회합니다.")
    public ResponseEntity<List<QuestRankResponse>> getQuestRankings(@PathVariable(name = "questId") final Long questId) {
        return ResponseEntity.ok(questRankService.findByQuestId(questId));
    }

    // 퀘스트 참여자 목록 조회 (참여자용)
    @GetMapping("/{questId}/memberQuest")
    @Operation(summary = "퀘스트 참여자 조회", description = "[참여자용] 특정 퀘스트의 참여자 목록을 조회합니다.")
    public ResponseEntity<MemberQuestListResponse> getQuestParticipants(@PathVariable(name = "questId") final Long questId) {
        List<MemberQuestResponse> memberQuests = memberQuestService.findByQuestId(questId);
        return ResponseEntity.ok(MemberQuestListResponse.of(memberQuests));
    }

    // 퀘스트 참여 신청 (참여자용)
    @PostMapping("/{questId}/memberQuest")
    @Operation(summary = "퀘스트 참여 신청", description = "[참여자용] 특정 퀘스트에 참여 신청을 합니다.")
    public ResponseEntity<ApiResponse> participateInQuest(@PathVariable(name = "questId") final Long questId) {
//        // questId를 request에 설정
//        memberQuestRequest.setQuest(questId);
//        MemberQuest createdMemberQuest = memberQuestService.createAndReturnEntity(memberQuestRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 침여 완료"));
    }

    // 증빙 자료 제출 (참여자용)
    @PostMapping("/memberQuest/{memberQuestId}/evidence")
    @Operation(summary = "증빙 자료 제출", description = "[참여자용] 퀘스트 참여에 대한 증빙 자료를 제출합니다.")
    public ResponseEntity<ApiResponse> submitEvidence(@PathVariable(name = "memberQuestId") final Long memberQuestId,
            @RequestBody @Valid final MemberQuestEvidenceRequest evidenceRequest) {
        // memberQuestId를 request에 설정
        evidenceRequest.setMemberQuest(memberQuestId);
        memberQuestEvidenceService.create(evidenceRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "증빙 자료 제출 완료"));
    }

    // 참여자 증빙 자료 조회 (참여자용)
    @GetMapping("/memberQuest/{memberQuestId}/evidence")
    @Operation(summary = "참여자 증빙 자료 조회", description = "[참여자용] (추후 추가예정)특정 참여자의 증빙 자료를 조회합니다.")
    public ResponseEntity<List<MemberQuestEvidenceResponse>> getParticipantEvidence(@PathVariable(name = "memberQuestId") final Long memberQuestId) {
        return ResponseEntity.ok(memberQuestEvidenceService.findByMemberQuestId(memberQuestId));
    }

    // 증빙 자료 수정 (참여자용)
    @PutMapping("/evidence/{evidenceId}")
    @Operation(summary = "증빙 자료 수정", description = "[참여자용] 본인이 제출한 증빙 자료를 수정합니다.")
    public ResponseEntity<ApiResponse> updateEvidence(@PathVariable(name = "evidenceId") final Long evidenceId,
            @RequestBody @Valid final MemberQuestEvidenceRequest evidenceRequest) {
        memberQuestEvidenceService.update(evidenceId, evidenceRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "증빙 자료 수정 완료"));
    }

    // 증빙 자료 삭제 (참여자용)
    @DeleteMapping("/evidence/{evidenceId}")
    @Operation(summary = "증빙 자료 삭제", description = "[참여자용] 본인이 제출한 증빙 자료를 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteEvidence(@PathVariable(name = "evidenceId") final Long evidenceId) {
        memberQuestEvidenceService.delete(evidenceId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "증빙 자료 삭제 완료"));
    }

}
