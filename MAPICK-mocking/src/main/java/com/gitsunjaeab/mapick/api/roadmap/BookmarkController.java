package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.common.ApiResponse;
import com.gitsunjaeab.mapick.api.common.ResponseCode;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDTO;
import com.gitsunjaeab.mapick.application.roadmap.BookmarkService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/bookmarks", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(final BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    // 북마크 전체 조회 >> NOTE 프론트 데이터 확인용 (추후 삭제예정)
    @GetMapping
    public ResponseEntity<List<BookmarkDTO>> getAllBookmarks() {
        List<BookmarkDTO> bookmarkDTOS = bookmarkService.findAll();
        return ResponseEntity.ok(bookmarkDTOS);
    }

     // 로드맵 북마크
    @PostMapping("/{roadmapId}")
    public ResponseEntity<ApiResponse> createBookmark(@PathVariable(name = "roadmapId") final Long roadmapId) {
        // TODO: JWT 에서 memberId 추출
        Long memberId = 1L;

        bookmarkService.create(roadmapId, memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "북마크 등록 완료"));
    }

    // 로드맵 북마크 해제
    @DeleteMapping("/{likeId}")
    public ResponseEntity<ApiResponse> deleteBookmark(@PathVariable(name = "likeId") final Long likeId) {
        bookmarkService.delete(likeId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "북마크 해제 완료"));
    }

    // 사용자 북마크 목록 조회 (마이페이지)
    @GetMapping("/bookmarkedRoadmaps")
    public ResponseEntity<RoadmapListResponse> getMyBookmarkedRoadmaps() {
        // TODO: JWT 에서 memberId 추출
        Long memberId = 1L;

        return ResponseEntity.ok(bookmarkService.getBookmarkedRoadmaps(memberId));
    }
}
