package com.gitsunjaeab.mapick.application.api.comment;

import com.gitsunjaeab.mapick.application.api.comment.dto.internal.CommentAchievementDTO;
import com.gitsunjaeab.mapick.application.api.comment.dto.internal.CommentDTO;
import com.gitsunjaeab.mapick.application.api.comment.dto.response.CommentListResponse;
import com.gitsunjaeab.mapick.application.api.comment.dto.request.CommentRequest;
import com.gitsunjaeab.mapick.application.api.comment.dto.response.CommentResponse;
import com.gitsunjaeab.mapick.application.api.comment.dto.response.MemberCommentListResponse;
import com.gitsunjaeab.mapick.application.domain.comment.CommentService;
import com.gitsunjaeab.mapick.application.domain.auth.Principal;
import com.gitsunjaeab.mapick.application.domain.comment.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('USER')")
@RequestMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 지도 댓글 API
     */

    @Operation(summary = "지도 댓글 생성", description = "[사용자] 특정 로드맵/공유지도의 댓글 생성")
    @PostMapping("/roadmaps")
    public ResponseEntity<CommentResponse> createRoadmapComment(
        @AuthenticationPrincipal final Principal principal,
        @RequestBody @Valid final CommentRequest request
    ) {
        return createComment(principal, request);
    }

    @Operation(summary = "지도 댓글 목록 조회", description = "[사용자] 특정 로드맵/공유지도의 모든 댓글을 조회")
    @GetMapping("/roadmaps")
    public ResponseEntity<CommentListResponse> getRoadmapComments(@RequestParam final Long roadmapId) {

        final List<CommentDTO> commentDTOList= commentService.findAllCommentsInRoadmaps(roadmapId);
        final CommentListResponse response = CommentListResponse.get(commentDTOList);

        return ResponseEntity.ok(response);
    }


    /**
     * 퀘스트 댓글 API
     */

    @Operation(summary = "퀘스트 댓글 생성", description = "[사용자] 특정 퀘스트의 댓글 생성")
    @PostMapping("/quests")
    public ResponseEntity<CommentResponse> createQuestComment(
        @AuthenticationPrincipal final Principal principal,
        @RequestBody @Valid final CommentRequest request
    ) {
        return createComment(principal, request);
    }

    @Operation(summary = "퀘스트 댓글 목록 조회", description = "[사용자] 특정 퀘스트의 모든 댓글을 조회")
    @GetMapping("/quests")
    public ResponseEntity<CommentListResponse> getQuestComments(@RequestParam final Long questId) {

        final List<CommentDTO> commentDTOList= commentService.findAllCommentsInQuest(questId);
        final CommentListResponse response = CommentListResponse.get(commentDTOList);
  
        return ResponseEntity.ok(response);
    }


    /**
     * 댓글 조회, 수정, 삭제 (공통)
     */

    @Operation(summary = "특정 댓글 조회", description = "댓글 수정 시, 특정 댓글에 대한 정보 조회 필요")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(
        @PathVariable(name = "commentId") final Long commentId)
    {
        final CommentDTO dto = commentService.getComment(commentId);
        final CommentResponse response = CommentResponse.get(dto);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 수정", description = "[작성자] 특정 댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
        @AuthenticationPrincipal Principal principal,
        @PathVariable(name = "commentId") final Long commentId,
        @RequestBody @Valid final CommentRequest request) {

        final Long memberId = principal.getMember().getId();
        final CommentDTO commentDTO = commentService.update(commentId, memberId, request);
        final CommentResponse response = CommentResponse.update(commentDTO);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "댓글 삭제", description = "[작성자/게시글 작성자] 댓글을 작성한 본인이나 퀘스트/로드맵 작성자는 본인 게시물의 댓글 삭제 가능")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(
        @AuthenticationPrincipal Principal principal,
        @PathVariable(name = "commentId") final Long commentId) {

        final Long memberId = principal.getMember().getId();
        commentService.delete(commentId, memberId);
        final CommentResponse response = CommentResponse.delete();

        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 작성 댓글 조회
     */

    @Operation(summary = "사용자 작성 목록 조회", description = "[사용자] 마이페이지에서 자신이 작성한 모든 댓글을 조회")
    @GetMapping("/member")
    public ResponseEntity<MemberCommentListResponse> getAllCommentsByMember(
        @AuthenticationPrincipal Principal principal)
    {
        final Long memberId = principal.getMember().getId();
        final List<Comment> comments = commentService.findAllCommentsByMember(memberId);
        final MemberCommentListResponse response = MemberCommentListResponse.of(comments);

        return ResponseEntity.ok(response);
    }

    // 댓글 생성
    private ResponseEntity<CommentResponse> createComment(
        final Principal principal,
        final CommentRequest request
    ) {
        final Long memberId = principal.getMember().getId();
        final CommentAchievementDTO dto = commentService.create(request, memberId);

        CommentResponse response = dto.isAchievementUnlocked()
            ? CommentResponse.createWithAchievement(dto)
            : CommentResponse.create(dto.getComment());

        return ResponseEntity.ok(response);
    }
}
