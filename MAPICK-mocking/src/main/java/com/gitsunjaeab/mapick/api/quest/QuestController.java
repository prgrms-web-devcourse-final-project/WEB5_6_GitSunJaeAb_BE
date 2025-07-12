package com.gitsunjaeab.mapick.api.quest;

import com.gitsunjaeab.mapick.api.quest.dto.QuestResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestListResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRequest;
import com.gitsunjaeab.mapick.application.quest.MemberQuestService;
import com.gitsunjaeab.mapick.application.quest.QuestRankService;
import com.gitsunjaeab.mapick.application.quest.QuestService;
import com.gitsunjaeab.mapick.application.quest.MemberQuestEvidenceService;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRankResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestCommentRequest;
import com.gitsunjaeab.mapick.api.quest.dto.QuestCommentResponse;
import com.gitsunjaeab.mapick.application.quest.QuestCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;

@RestController
@RequestMapping(value = "/quests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "퀘스트 관리", description = "퀘스트 관련 API (관리자용/사용자용)")
public class QuestController {

    private final QuestService questService;
    private final QuestRankService questRankService;
    private final MemberQuestService memberQuestService;
    private final MemberQuestEvidenceService memberQuestEvidenceService;
    private final QuestCommentService questCommentService;

    public QuestController(final QuestService questService, final QuestRankService questRankService,
        final MemberQuestService memberQuestService,
        final MemberQuestEvidenceService memberQuestEvidenceService,
        final QuestCommentService questCommentService) {
        this.questService = questService;
        this.questRankService = questRankService;
        this.memberQuestService = memberQuestService;
        this.memberQuestEvidenceService = memberQuestEvidenceService;
        this.questCommentService = questCommentService;
    }

    // ===== 관리자용 API =====

    // 퀘스트 생성 (관리자용)
    @PostMapping
    @Operation(summary = "퀘스트 생성", description = "[관리자용] 새로운 퀘스트를 생성합니다.")
    public ResponseEntity<ApiResponse> createQuest(@RequestBody @Valid final QuestRequest questRequest) {
        questService.create(questRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 생성 완료"));
    }

    // 퀘스트 수정 (관리자용)
    @PutMapping("/{questsId}")
    @Operation(summary = "퀘스트 수정", description = "[관리자용] 기존 퀘스트의 정보를 수정합니다.")
    public ResponseEntity<ApiResponse> updateQuest(@PathVariable(name = "questsId") final Long questsId,
            @RequestBody @Valid final QuestRequest questRequest) {
        questService.update(questsId, questRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 수정 완료"));
    }

    // 퀘스트 삭제 (관리자용)
    @DeleteMapping("/{questsId}")
    @Operation(summary = "퀘스트 삭제", description = "[관리자용] 퀘스트를 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteQuest(@PathVariable(name = "questsId") final Long questsId) {
        final ReferencedWarning referencedWarning = questService.getReferencedWarning(questsId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        questService.delete(questsId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 삭제 완료"));
    }

    // 전체 증빙 자료 조회 (관리자용)
    @GetMapping("/evidence")
    @Operation(summary = "전체 증빙 자료 조회", description = "[관리자용] 모든 퀘스트의 증빙 자료를 조회합니다.")
    public ResponseEntity<List<MemberQuestEvidenceResponse>> getAllEvidence() {
        return ResponseEntity.ok(memberQuestEvidenceService.findAll());
    }

    // 증빙 자료 삭제 (관리자용)
    @DeleteMapping("/evidence/{evidenceId}")
    @Operation(summary = "증빙 자료 삭제", description = "[관리자용] 특정 증빙 자료를 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteEvidence(@PathVariable(name = "evidenceId") final Long evidenceId) {
        memberQuestEvidenceService.delete(evidenceId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "증빙 자료 삭제 완료"));
    }

    // ===== 사용자용 API =====

    // 전체 퀘스트 조회 (사용자용)
    @GetMapping
    @Operation(summary = "전체 퀘스트 조회", description = "[사용자용] 활성화된 퀘스트 목록을 조회합니다.")
    public ResponseEntity<QuestListResponse> getAllQuests() {
        return ResponseEntity.ok(QuestListResponse.of(questService.findAll()));
    }

    // 단일 퀘스트 조회 (사용자용)
    @GetMapping("/{questsId}")
    @Operation(summary = "퀘스트 상세 조회", description = "[사용자용] 특정 퀘스트의 상세 정보를 조회합니다.")
    public ResponseEntity<QuestResponse> getQuest(@PathVariable(name = "questsId") final Long questsId) {
        return ResponseEntity.ok(questService.get(questsId));
    }

    // 퀘스트 랭킹 조회 (사용자용)
    @GetMapping("/{questId}/rankings")
    @Operation(summary = "퀘스트 랭킹 조회", description = "[사용자용] 특정 퀘스트의 랭킹을 조회합니다.")
    public ResponseEntity<List<QuestRankResponse>> getQuestRankings(@PathVariable(name = "questId") final Long questId) {
        return ResponseEntity.ok(questRankService.findByQuestId(questId));
    }

    // 퀘스트 참여자 목록 조회 (사용자용)
    @GetMapping("/{questId}/memberQuest")
    @Operation(summary = "퀘스트 참여자 조회", description = "[사용자용] 특정 퀘스트의 참여자 목록을 조회합니다.")
    public ResponseEntity<List<MemberQuestResponse>> getQuestParticipants(@PathVariable(name = "questId") final Long questId) {
        return ResponseEntity.ok(memberQuestService.findByQuestId(questId));
    }

    // 퀘스트 참여 신청 (사용자용)
    @PostMapping("/{questId}/memberQuest")
    @Operation(summary = "퀘스트 참여 신청", description = "[사용자용] 특정 퀘스트에 참여 신청을 합니다.")
    public ResponseEntity<ApiResponse> participateInQuest(@PathVariable(name = "questId") final Long questId,
            @RequestBody @Valid final MemberQuestRequest memberQuestRequest) {
        // questId를 request에 설정
        memberQuestRequest.setQuest(questId);
        memberQuestService.create(memberQuestRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 참여 신청 완료"));
    }

    // 증빙 자료 제출 (사용자용)
    @PostMapping("/memberQuest/{memberQuestId}/evidence")
    @Operation(summary = "증빙 자료 제출", description = "[사용자용] 퀘스트 참여에 대한 증빙 자료를 제출합니다.")
    public ResponseEntity<ApiResponse> submitEvidence(@PathVariable(name = "memberQuestId") final Long memberQuestId,
            @RequestBody @Valid final MemberQuestEvidenceRequest evidenceRequest) {
        // memberQuestId를 request에 설정
        evidenceRequest.setMemberQuest(memberQuestId);
        memberQuestEvidenceService.create(evidenceRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "증빙 자료 제출 완료"));
    }

    // 특정 참여자의 증빙 자료 조회 (사용자용)
    @GetMapping("/memberQuest/{memberQuestId}/evidence")
    @Operation(summary = "참여자 증빙 자료 조회", description = "[사용자용] 특정 참여자의 증빙 자료를 조회합니다.")
    public ResponseEntity<List<MemberQuestEvidenceResponse>> getParticipantEvidence(@PathVariable(name = "memberQuestId") final Long memberQuestId) {
        return ResponseEntity.ok(memberQuestEvidenceService.findByMemberQuestId(memberQuestId));
    }

    // ===== 퀘스트 댓글 관련 API (사용자용) =====

    // 특정 퀘스트의 모든 댓글 조회 (사용자용)
    @GetMapping("/{questId}/comments")
    @Operation(summary = "퀘스트 댓글 조회", description = "[사용자용] 특정 퀘스트의 모든 댓글을 조회합니다.")
    public ResponseEntity<List<QuestCommentResponse>> getQuestComments(@PathVariable final Long questId) {
        return ResponseEntity.ok(questCommentService.findByQuestId(questId));
    }

    // 특정 댓글 조회 (사용자용)
    @GetMapping("/comments/{commentId}")
    @Operation(summary = "댓글 상세 조회", description = "[사용자용] 특정 댓글의 상세 정보를 조회합니다.")
    public ResponseEntity<QuestCommentResponse> getQuestComment(@PathVariable final Long commentId) {
        return ResponseEntity.ok(questCommentService.get(commentId));
    }

    // 댓글 생성 (사용자용)
    @PostMapping("/{questId}/comments")
    @Operation(summary = "댓글 생성", description = "[사용자용] 퀘스트에 댓글을 작성합니다.")
    public ResponseEntity<ApiResponse> createQuestComment(@PathVariable final Long questId,
            @RequestBody @Valid final QuestCommentRequest questCommentRequest) {
        // questId를 request에 설정
        questCommentRequest.setQuest(questId);
        final Long createdId = questCommentService.create(questCommentRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 댓글이 성공적으로 생성되었습니다. ID: " + createdId));
    }

    // 댓글 수정 (사용자용)
    @PutMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "[사용자용] 자신이 작성한 댓글을 수정합니다.")
    public ResponseEntity<ApiResponse> updateQuestComment(@PathVariable final Long commentId,
            @RequestBody @Valid final QuestCommentRequest questCommentRequest) {
        questCommentService.update(commentId, questCommentRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 댓글이 성공적으로 수정되었습니다."));
    }

    // 댓글 삭제 (사용자용)
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "[사용자용] 자신이 작성한 댓글을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteQuestComment(@PathVariable final Long commentId) {
        questCommentService.delete(commentId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 댓글이 성공적으로 삭제되었습니다."));
    }

}
