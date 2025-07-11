package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDTO;
import com.gitsunjaeab.mapick.application.roadmap.BookmarkService;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository.RoadmapCitationProjection;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
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
    private final LayerLibraryRepository layerLibraryRepository;

    public BookmarkController(final BookmarkService bookmarkService,
        final LayerLibraryRepository layerLibraryRepository) {
        this.bookmarkService = bookmarkService;
        this.layerLibraryRepository = layerLibraryRepository;
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

    // 로드맵 좋아요 목록 조회 (마이페이지)
    @GetMapping("/bookmarkedRoadmaps")
    public ResponseEntity<RoadmapListResponse> getMyBookmarkedRoadmaps() {
        // TODO: JWT 에서 memberId 추출
        Long memberId = 1L; // memberId = 1

        List<Roadmap> roadmaps = bookmarkService.getBookmarkedRoadmaps(memberId);

        // 로드맵 별 인용수 조회
        List<Long> roadmapIds = roadmaps.stream()
            .map(Roadmap::getId)
            .collect(Collectors.toList());

        List<RoadmapCitationProjection> projections = layerLibraryRepository.countDistinctMemberByRoadmapIds(roadmapIds);

        Map<Long, Long> citationCountMap = projections.stream() // 로드맵 ID (key), 인용수 (value)
            .collect(Collectors.toMap(
                RoadmapCitationProjection::getRoadmapId,
                RoadmapCitationProjection::getCitationCount
            ));

        return ResponseEntity.ok(RoadmapListResponse.of(roadmaps, citationCountMap));
    }
}
