package com.gitsunjaeab.mapick.comment.controller;

import com.gitsunjaeab.mapick.comment.CommentService;
import com.gitsunjaeab.mapick.comment.dto.CommentDTO;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.member.MemberRepository;
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
@RequestMapping(value = "/roadmaps/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

    private final CommentService commentService;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public CommentController(final CommentService commentService, final RoadmapRepository roadmapRepository,
            final MemberRepository memberRepository) {
        this.commentService = commentService;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
    }

    // 로드맵 댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }

    // 댓글 생성(작성)
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createComment(@RequestBody @Valid final CommentDTO commentDTO) {
        final Long createdId = commentService.create(commentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Long> updateComment(@PathVariable(name = "commentId") final Long commentId,
            @RequestBody @Valid final CommentDTO commentDTO) {
        commentService.update(commentId, commentDTO);
        return ResponseEntity.ok(commentId);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "commentId") final Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CommentDTO> getComment(@PathVariable(name = "id") final Long id) {
//        return ResponseEntity.ok(commentService.get(id));
//    }
//
//    @GetMapping("/mapValues")
//    public ResponseEntity<Map<Long, String>> getMapValues() {
//        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.entity.Map::getId, com.gitsunjaeab.mapick.map.entity.Map::getTitle)));
//    }
//
//    @GetMapping("/memberValues")
//    public ResponseEntity<Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }

}
