package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkCreateResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDeleteResponse;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDTO;
import com.gitsunjaeab.mapick.application.roadmap.BookmarkService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/bookmarks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "북마크 관리 API", description = "로드맵 북마크 및 마이페이지 북마크 목록 관련 API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(final BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    // 북마크 전체 조회 >> NOTE 프론트 데이터 확인용 (추후 삭제예정)
    @GetMapping
    @Operation(summary = "북마크 전체 조회", description = "데이터 확인용 API (추후 삭제 예정)")
    public ResponseEntity<List<BookmarkDTO>> getAllBookmarks() {
        List<BookmarkDTO> bookmarkDTOS = bookmarkService.findAll();
        return ResponseEntity.ok(bookmarkDTOS);
    }

     // 지도(로드맵, 공유지도) 북마크 생성
    @PostMapping("/{roadmapId}")
    @Operation(summary = "지도(로드맵, 공유지도) 북마크", description = "[사용자용] 로드맵이나 공유지도를 북마크")
    public ResponseEntity<BookmarkCreateResponse> createBookmark(
            @PathVariable(name = "roadmapId") final Long roadmapId,
            @AuthenticationPrincipal Principal principal) {

        Long memberId = principal.getMember().getId();

        bookmarkService.create(roadmapId, memberId);

        return ResponseEntity.ok(BookmarkCreateResponse.of(ResponseCode.OK, "북마크 등록 완료"));
    }

    // 로드맵 북마크 해제
    @DeleteMapping("/{likeId}")
    @Operation(summary = "지도(로드맵, 공유지도) 북마크 해제", description = "[사용자용] 로드맵이나 공유지도 북마크를 해제")
    public ResponseEntity<BookmarkDeleteResponse> deleteBookmark(
            @PathVariable(name = "likeId") final Long likeId
            ,@AuthenticationPrincipal Principal principal) {
        Long memberId = principal.getMember().getId();
        bookmarkService.delete(likeId, memberId);

        return ResponseEntity.ok(BookmarkDeleteResponse.of(ResponseCode.OK, "북마크 해제 완료"));
    }

    // 사용자 북마크 목록 조회 (마이페이지)
    @GetMapping("/bookmarkedRoadmaps")
    @Operation(summary = "사용자의 지도(로드맵, 공유지도) 북마크 목록 조회", description = "[사용자용] 마이페이지에서 북마크한 지도 목록을 조회")
    public ResponseEntity<RoadmapListResponse> getMyBookmarkedRoadmaps(
            @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();

        return ResponseEntity.ok(bookmarkService.getBookmarkedRoadmaps(memberId));
    }
}
