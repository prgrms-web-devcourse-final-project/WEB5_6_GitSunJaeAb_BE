package com.gitsunjaeab.mapick.api.comment;

import com.gitsunjaeab.mapick.api.comment.dto.CommentListResponse;
import com.gitsunjaeab.mapick.api.comment.dto.CommentRequest;
import com.gitsunjaeab.mapick.application.comment.CommentService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "댓글 관리 API", description = "지도 및 퀘스트의 댓글 관리 API")
public class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService){
        this.commentService = commentService;
    }

    // ===== 지도 댓글 관련 API =====

    // 지도(개인 로드맵, 공유지도) 댓글 전체 조회
    @GetMapping("/roadmaps")
    @Operation(summary = "지도 댓글 목록 조회", description = "[사용자용] 로드맵이나 공유지도의 댓글 목록을 조회")
    public ResponseEntity<CommentListResponse> getAllCommentsInMaps(
        @RequestParam(required = false) Long roadmapId
    ) {
        return ResponseEntity.ok(commentService.findAllCommentsInRoadmaps(roadmapId));
    }

    // 지도 댓글 생성(작성)
    @PostMapping("/roadmaps")
    @Operation(summary = "지도 댓글 생성", description = "[사용자용] 로드맵이나 공유지도의 댓글 생성")
    public ResponseEntity<ApiResponse> createComment(@RequestBody @Valid final CommentRequest request) {
//        final Long createdId = commentService.create(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 작성 완료"));
    }

    // 지도 댓글 수정
    @PutMapping("/roadmaps/{commentId}")
    @Operation(summary = "지도 댓글 수정", description = "[사용자용] 로드맵이나 공유지도의 댓글 수정")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable(name = "commentId") final Long commentId,
            @RequestBody @Valid final CommentRequest request) {
//        commentService.update(commentId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 수정 완료"));
    }

    // 지도 댓글 삭제
    @DeleteMapping("/roadmaps/{commentId}")
    @Operation(summary = "지도 댓글 삭제", description = "[사용자용] 로드맵이나 공유지도의 댓글 삭제")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable(name = "commentId") final Long commentId) {
//        commentService.delete(commentId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 삭제 완료"));
    }

    // ===== 퀘스트 댓글 관련 API =====

    // 퀘스트 댓글 조회 (쿼리 파라미터 방식)
    @GetMapping("/quests")
    @Operation(summary = "퀘스트 댓글 목록 조회", description = "[모든 사용자] 특정 퀘스트의 모든 댓글을 조회합니다.")
    public ResponseEntity<CommentListResponse> getQuestComments(@RequestParam Long questId) {
        return ResponseEntity.ok(commentService.findAllCommentsInQuest(questId));
    }

    // 퀘스트 댓글 생성
    @PostMapping("/quests")
    @Operation(summary = "퀘스트 댓글 생성", description = "[모든 사용자] 퀘스트에 댓글을 작성합니다.")
    public ResponseEntity<ApiResponse> createQuestComment(
            @RequestBody @Valid final CommentRequest request) {
        // questId를 request에 설정
//        questCommentRequest.setQuest(questId);
//        Long commentId = questCommentService.create(questCommentRequest);
//        QuestCommentResponse createdComment = questCommentService.get(commentId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 댓글 생성 완료"));
    }

    // 퀘스트 댓글 수정
    @PutMapping("/quests/{commentId}")
    @Operation(summary = "퀘스트 댓글 수정", description = "[댓글 작성자] 자신이 작성한 댓글을 수정합니다.")
    public ResponseEntity<ApiResponse> updateQuestComment(
            @RequestParam Long questId,
            @RequestBody @Valid final CommentRequest request) {
        // questId 설정 (일관성 유지)
//        questCommentRequest.setQuest(questId);
//        questCommentService.update(commentId, questCommentRequest);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 댓글 수정 완료"));
    }

    // 퀘스트 댓글 삭제
    @DeleteMapping("/quests/{commentId}")
    @Operation(summary = "퀘스트 댓글 삭제", description = "[댓글 작성자] 자신이 작성한 댓글을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteQuestComment(
            @RequestParam Long questId) {
//        questCommentService.delete(commentId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 댓글 삭제 완료"));
    }

}
