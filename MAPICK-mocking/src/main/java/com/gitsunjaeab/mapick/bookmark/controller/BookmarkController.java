package com.gitsunjaeab.mapick.bookmark.controller;

import com.gitsunjaeab.mapick.bookmark.BookmarkService;
import com.gitsunjaeab.mapick.bookmark.dto.BookmarkDTO;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.member.MemberRepository;
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
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public BookmarkController(final BookmarkService bookmarkService,
            final RoadmapRepository roadmapRepository, final MemberRepository memberRepository) {
        this.bookmarkService = bookmarkService;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
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

//    @GetMapping
//    public ResponseEntity<List<BookmarkDTO>> getAllBookmarks() {
//        return ResponseEntity.ok(bookmarkService.findAll());
//    }

//    @GetMapping("/{likeId}")
//    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable(name = "likeId") final Long likeId) {
//        return ResponseEntity.ok(bookmarkService.get(likeId));
//    }
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Long> updateBookmark(@PathVariable(name = "id") final Long id,
//            @RequestBody @Valid final BookmarkDTO bookmarkDTO) {
//        bookmarkService.update(id, bookmarkDTO);
//        return ResponseEntity.ok(id);
//    }


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
