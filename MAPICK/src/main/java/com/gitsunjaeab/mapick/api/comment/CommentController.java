package com.gitsunjaeab.mapick.api.comment;

import com.gitsunjaeab.mapick.api.comment.dto.CommentDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentListResponse;
import com.gitsunjaeab.mapick.api.comment.dto.CommentRequest;
import com.gitsunjaeab.mapick.api.comment.dto.CommentResponse;
import com.gitsunjaeab.mapick.api.comment.dto.MemberCommentListResponse;
import com.gitsunjaeab.mapick.application.comment.CommentService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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


@Tag(name = "댓글 관리 API", description = "지도 및 퀘스트의 댓글 관리 API")
@RestController
@RequestMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 지도 댓글 API
     */

    // 지도 댓글 생성
    @PostMapping("/roadmaps")
    @Operation(summary = "지도 댓글 생성", description = "[모든 사용자] 특정 로드맵/공유지도의 댓글 생성")
    public ResponseEntity<ApiResponse> createComment(
        @AuthenticationPrincipal Principal principal,
        @RequestBody @Valid final CommentRequest request) {
        Long memberId = principal.getMember().getId();
        commentService.create(request, memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 작성 완료"));
    }

    // 특정 지도 댓글 조회
    @GetMapping("/roadmaps")
    @Operation(summary = "지도 댓글 목록 조회", description = "[모든 사용자] 특정 로드맵/공유지도의 모든 댓글을 조회")
    public ResponseEntity<CommentListResponse> getAllCommentsInMaps(@RequestParam Long roadmapId) {

        return ResponseEntity.ok(commentService.findAllCommentsInRoadmaps(roadmapId));
    }

    /**
     * 퀘스트 댓글 API
     */

    // 퀘스트 댓글 생성
    @PostMapping("/quests")
    @Operation(summary = "퀘스트 댓글 생성", description = "[모든 사용자] 특정 퀘스트의 댓글 생성")
    public ResponseEntity<ApiResponse> createQuestComment(
        @AuthenticationPrincipal Principal principal,
        @RequestBody @Valid final CommentRequest request) {
        Long memberId = principal.getMember().getId();
        commentService.create(request, memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "퀘스트 댓글 생성 완료"));
    }

    // 특정 퀘스트 댓글 조회
    @GetMapping("/quests")
    @Operation(summary = "퀘스트 댓글 목록 조회", description = "[모든 사용자] 특정 퀘스트의 모든 댓글을 조회")
    public ResponseEntity<CommentListResponse> getQuestComments(@RequestParam Long questId) {

        return ResponseEntity.ok(commentService.findAllCommentsInQuest(questId));
    }

    /**
     * 댓글 조회, 수정, 삭제 (공통)
     */

    // 특정 댓글 조회
    @GetMapping("/{commentId}")
    @Operation(summary = "특정 댓글 조회", description = "댓글 수정 시, 특정 댓글에 대한 정보 조회 필요")
    public ResponseEntity<CommentResponse> getComment(
        @PathVariable(name = "commentId") final Long commentId)
    {
        CommentDTO dto = commentService.getComment(commentId);
        CommentResponse response = CommentResponse.of(dto);

        return ResponseEntity.ok(response);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "[댓글 작성자/게시글 작성자] 특정 댓글 수정")
    public ResponseEntity<ApiResponse> updateRoadmapComment(
        @PathVariable(name = "commentId") final Long commentId,
        @RequestBody @Valid final CommentRequest request) {
        commentService.update(commentId, request);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 수정 완료"));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "[댓글 작성자/게시글 작성자] 댓글 삭제")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable(name = "commentId") final Long commentId) {
        commentService.delete(commentId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 삭제 완료"));
    }

    /**
     * 사용자 작성 댓글 조회
     */

    @GetMapping("/member")
    @Operation(summary = "사용자 작성 목록 조회", description = "[사용자] 마이페이지에서 자신이 작성한 모든 댓글을 조회")
    public ResponseEntity<MemberCommentListResponse> getAllCommentsByMember(
        @AuthenticationPrincipal Principal principal)
    {
        Long memberId = principal.getMember().getId();
        List<Comment> comments = commentService.findAllCommentsByMember(memberId);
        MemberCommentListResponse response = MemberCommentListResponse.of(comments);

        return ResponseEntity.ok(response);
    }

}
