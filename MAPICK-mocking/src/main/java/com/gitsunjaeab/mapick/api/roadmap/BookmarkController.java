package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDTO;
import com.gitsunjaeab.mapick.application.roadmap.BookmarkService;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/roadmaps/like", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(final BookmarkService bookmarkService,
            final RoadmapRepository roadmapRepository, final MemberRepository memberRepository) {
        this.bookmarkService = bookmarkService;
    }

     // 로드맵 좋아요
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBookmark(@RequestBody @Valid final BookmarkDTO bookmarkDTO) {
        final Long createdId = bookmarkService.create(bookmarkDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    // 로드맵 좋아요 해제
    @DeleteMapping("/{likeId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBookmark(@PathVariable(name = "likeId") final Long likeId) {
        bookmarkService.delete(likeId);
        return ResponseEntity.noContent().build();
    }
}
