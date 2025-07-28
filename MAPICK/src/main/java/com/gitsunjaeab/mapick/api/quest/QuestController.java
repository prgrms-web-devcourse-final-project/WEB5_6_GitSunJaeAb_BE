package com.gitsunjaeab.mapick.api.quest;

import com.gitsunjaeab.mapick.api.quest.dto.*;
import com.gitsunjaeab.mapick.application.quest.MemberQuestService;
import com.gitsunjaeab.mapick.application.quest.QuestRankService;
import com.gitsunjaeab.mapick.application.quest.QuestService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/quests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "퀘스트 관리", description = "퀘스트 관련 API (출제자용/참여자용)")
public class QuestController {

    private final QuestService questService;
    private final QuestRankService questRankService;
    private final MemberQuestService memberQuestService;

    public QuestController(QuestService questService, QuestRankService questRankService, MemberQuestService memberQuestService) {
        this.questService = questService;
        this.questRankService = questRankService;
        this.memberQuestService = memberQuestService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "퀘스트 생성", description = "[출제자용] 새로운 퀘스트를 생성합니다. 본인만 접근 가능합니다.")
    public ResponseEntity<ApiResponse> createQuest(
        @RequestPart(name = "questRequest") @Valid QuestRequest questRequest,
        @RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
        @AuthenticationPrincipal Principal principal) {

        if (principal == null) throw new IllegalStateException("인증된 유저 정보 없음");

        Member member = principal.getMember();
        QuestAchievementResponse response = questService.create(questRequest, member, imageFile);

        if (response.isAchievementUnlocked()) {
            return ResponseEntity.ok(ApiResponse.of(
                ResponseCode.OK,
                "퀘스트 생성 완료! 업적 '" + response.getAchievement().getName() + "' 을(를) 획득했습니다.",
                response));
        }

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 생성 완료", response.getQuestId()));
    }

    @PutMapping(value = "/{questsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "퀘스트 수정", description = "[출제자용] 본인이 생성한 퀘스트의 정보를 수정합니다.")
    public ResponseEntity<ApiResponse> updateQuest(
        @PathVariable(name = "questsId") Long questsId,
        @RequestPart(name = "questRequest") @Valid QuestRequest questRequest,
        @RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
        @AuthenticationPrincipal Principal principal) {

        if (principal == null) throw new IllegalStateException("인증된 유저 정보 없음");

        questService.update(questsId, questRequest, principal.getMember().getEmail(), imageFile);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 수정 완료"));
    }

    @DeleteMapping("/{questsId}")
    @Operation(summary = "퀘스트 삭제", description = "[출제자용] 본인이 생성한 퀘스트를 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteQuest(
        @PathVariable(name = "questsId") Long questsId,
        @AuthenticationPrincipal Principal principal) {

        ReferencedWarning referencedWarning = questService.getReferencedWarning(questsId);
        if (referencedWarning != null) throw new ReferencedException(referencedWarning);

        questService.delete(questsId, principal.getMember().getEmail());
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 삭제 완료"));
    }

    @GetMapping
    @Operation(summary = "전체 퀘스트 조회", description = "[참여자용] 활성화된 퀘스트 목록을 조회합니다.")
    public ResponseEntity<QuestListResponse> getAllQuests(@RequestParam(required = false) Boolean isActive) {
        return ResponseEntity.ok(QuestListResponse.of(questService.findAll(isActive)));
    }

    // 단순 퀘스트 정보만
    @GetMapping("/{questsId}")
    @Operation(summary = "퀘스트 상세 조회", description = "[참여자용] 특정 퀘스트의 상세 정보를 조회합니다.")
    public ResponseEntity<QuestDetailResponse> getQuest(@PathVariable(name = "questsId") Long questsId) {
        return ResponseEntity.ok(QuestDetailResponse.of(questService.get(questsId)));
    }

    //퀘스트 + 참여자 답안 + 랭킹 까지
    @GetMapping("/{questId}/detail")
    @Operation(summary = "퀘스트 전체 상세 조회", description = "[모두]퀘스트 정보, 제출 목록, 랭킹을 함께 조회")
    public ResponseEntity<QuestFullDetailResponse> getQuestFullDetail(@PathVariable(name = "questId") Long questId) {

        QuestResponse quest = questService.get(questId); //퀘스트 정보
        List<MemberQuestSubmissionDTO> submissions = memberQuestService.getSubmissions(questId); //인증 리스트
        List<MemberRankingDTO> ranking = memberQuestService.getRanking(questId); //랭킹 정보

        return ResponseEntity.ok(QuestFullDetailResponse.of(quest, submissions, ranking));
    }

    @GetMapping("/{questId}/rankings")
    @Operation(summary = "퀘스트 랭킹 조회", description = "[참여자용] 특정 퀘스트의 랭킹을 조회합니다.")
    public ResponseEntity<List<QuestRankResponse>> getQuestRankings(@PathVariable(name = "questId") Long questId) {
        return ResponseEntity.ok(questRankService.findByQuestId(questId));
    }

    @GetMapping("/{questId}/memberQuest")
    @Operation(summary = "퀘스트 참여자 조회", description = "[참여자용] 특정 퀘스트의 참여자 목록을 조회합니다.")
    public ResponseEntity<MemberQuestListResponse> getQuestParticipants(@PathVariable(name = "questId") Long questId) {
        return ResponseEntity.ok(MemberQuestListResponse.of(memberQuestService.findByQuestId(questId)));
    }

    @GetMapping("/memberQuest/my")
    @Operation(summary = "내가 참여한 퀘스트 목록 조회", description = "현재 로그인한 사용자가 참여한 퀘스트 목록을 조회합니다.")
    public ResponseEntity<MemberQuestListResponse> getMyParticipatedQuests(
        @AuthenticationPrincipal Principal principal) {
        if (principal == null) throw new IllegalStateException("인증된 유저 정보 없음");

        Member member = principal.getMember();
        return ResponseEntity.ok(MemberQuestListResponse.of(memberQuestService.findByMember(member)));
    }

    @PostMapping(value = "/{questId}/memberQuest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "퀘스트 참여 신청 및 증빙자료 제출", description = "[참여자용] 특정 퀘스트에 참여 신청을 합니다.")
    public ResponseEntity<ApiResponse> participateInQuest(
        @PathVariable(name = "questId") Long questId,
        @AuthenticationPrincipal Principal principal,
        @RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
        @RequestPart(name = "request") @Valid MemberQuestCreateRequest request) {

        if (principal == null) throw new IllegalStateException("인증된 유저 정보 없음");

        request.setQuestId(questId);
        Member member = principal.getMember();
        MemberQuestCreateResponse response = memberQuestService.createMemberQuest(request, member, imageFile);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 참여 완료"));
    }

    @PutMapping(value = "/memberQuest/{memberQuestId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "퀘스트 참여 정보 수정", description = "[참여자용] 본인이 제출한 퀘스트 참여 내용을 수정합니다.")
    public ResponseEntity<ApiResponse> updateMemberQuest(
        @PathVariable(name = "memberQuestId") Long memberQuestId,
        @AuthenticationPrincipal Principal principal,
        @RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
        @RequestPart(name = "request") @Valid MemberQuestUpdateRequest request) {

        if (principal == null) throw new IllegalStateException("인증된 유저 정보 없음");

        Member member = principal.getMember();
        MemberQuestUpdateResponse response = memberQuestService.updateMemberQuest(memberQuestId, request, member, imageFile);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 참여 정보 수정 완료"));
    }

    @PutMapping("/memberQuest/judge")
    @Operation(summary = "퀘스트 판별", description = "[출제자용] 퀘스트 참여자의 제출물을 보고 정답 여부를 판별합니다.")
    public ResponseEntity<ApiResponse> judgeMemberQuest(
        @RequestBody @Valid MemberQuestJudgeRequest request,
        @AuthenticationPrincipal Principal principal) {

        if (principal == null) throw new IllegalStateException("인증된 유저 정보 없음");

        Member judgeMember = principal.getMember();
        MemberQuestJudgeResponse response = memberQuestService.judgeMemberQuest(request, judgeMember);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 판별 완료"));
    }

    @GetMapping("/{questId}/ranking")
    @Operation(summary = "퀘스트 정답자 랭킹 조회", description = "정답 맞춘 순서대로 퀘스트 참여자 랭킹을 조회합니다.")
    public ResponseEntity<List<MemberQuestRankResponse>> getQuestRankingByCorrectOrder(
        @PathVariable(name = "questId") Long questId
    ) {
        List<MemberQuestRankResponse> ranking = memberQuestService.getRankedMembersByQuest(questId);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/{questId}/ranking/top3")
    @Operation(summary = "퀘스트 상위 3명 랭킹 조회", description = "정답 맞춘 순서 기준으로 상위 3명만 조회합니다.")
    public ResponseEntity<List<MemberQuestRankResponse>> getTop3QuestRanking(
        @PathVariable(name = "questId") Long questId
    ) {
        List<MemberQuestRankResponse> top3 = memberQuestService.getTop3RankedMembersByQuest(questId);
        return ResponseEntity.ok(top3);
    }


}
