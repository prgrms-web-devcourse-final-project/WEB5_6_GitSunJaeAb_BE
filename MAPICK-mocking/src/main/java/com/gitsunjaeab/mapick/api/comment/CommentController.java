package com.gitsunjaeab.mapick.api.comment;

import com.gitsunjaeab.mapick.api.comment.dto.CommentListResponse;
import com.gitsunjaeab.mapick.api.comment.dto.CommentRequest;
import com.gitsunjaeab.mapick.application.roadmap.CommentService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
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
public class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService, final RoadmapRepository roadmapRepository,
            final MemberRepository memberRepository) {
        this.commentService = commentService;
    }

    // 지도(개인 로드맵, 공유지도) 댓글 전체 조회
    @GetMapping("/roadmaps")
    public ResponseEntity<CommentListResponse> getAllCommentsInMaps(
        @RequestParam(required = false) Long roadmapId
    ) {
        return ResponseEntity.ok(commentService.findAllCommentsInRoadmaps(roadmapId));
    }

    // 지도 댓글 생성(작성)
    @PostMapping("/roadmaps")
    public ResponseEntity<ApiResponse> createComment(@RequestBody @Valid final CommentRequest request) {
//        final Long createdId = commentService.create(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 작성 완료"));
    }


    // 지도 댓글 수정
    @PutMapping("/roadmaps/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable(name = "commentId") final Long commentId,
            @RequestBody @Valid final CommentRequest request) {
//        commentService.update(commentId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 수정 완료"));
    }

    // 지도 댓글 삭제
    @DeleteMapping("/roadmaps/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable(name = "commentId") final Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "댓글 삭제 완료"));
    }
}
