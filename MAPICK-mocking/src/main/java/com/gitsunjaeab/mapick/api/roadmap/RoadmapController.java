package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapRequest;
import com.gitsunjaeab.mapick.application.roadmap.RoadmapService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
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
@RequestMapping(value = "/roadmaps", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(final RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    // 전체 조회 (로드맵, 공유지도) >> NOTE 프론트 데이터 확인용 (추후 삭제예정)
    @GetMapping
    public ResponseEntity<RoadmapListResponse> getAllRoadmaps() {
        RoadmapListResponse response = roadmapService.getAllRoadmaps();
        return ResponseEntity.ok(response);
    }

    // 로드맵(PERSONAL) 조회
    @GetMapping("/personal")
    public ResponseEntity<RoadmapListResponse> getPersonalRoadmaps(
        @RequestParam(required = false) Long categoryId) {
        if (categoryId == null) {
            // 전체 조회
            return ResponseEntity.ok(roadmapService.getAllPersonalRoadmapsWithCitation());
        } else {
            // 카테고리별 조회
            return ResponseEntity.ok(roadmapService.getPersonalRoadmapsByCategory(categoryId));
        }
    }

    // 공유지도(SHARED) 조회
    @GetMapping("/shared")
    public ResponseEntity<RoadmapListResponse> getSharedRoadmaps(
        @RequestParam(required = false) Long categoryId) {
        if (categoryId == null) {
            // 전체 조회
            return ResponseEntity.ok(roadmapService.getAllSharedRoadmapsWithCitation());
        } else {
            // 카테고리별 조회
            return ResponseEntity.ok(roadmapService.getSharedRoadmapsByCategory(categoryId));
        }
    }

    // TODO 해시태그로 로드맵 조회 >> {hashtagId} 활용??

    // 로드맵 생성
    @PostMapping("/personal")
    public ResponseEntity<ApiResponse> createPersonalRoadmap(@RequestBody @Valid final RoadmapRequest request) {
//        final Long createdId = roadmapService.create(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "로드맵 생성 완료"));
    }

    @PostMapping("/shared")
    public ResponseEntity<ApiResponse> createSharedRoadmap(@RequestBody @Valid final RoadmapRequest request) {
//        final Long createdId = roadmapService.create(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "공유지도 생성 완료"));
    }

    // TODO 특정 회원의 지도 목록 조회

    // 특정 지도 상세 조회
    @GetMapping("/{roadmapId}")
    public ResponseEntity<RoadmapDTO> getRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId) {
        return ResponseEntity.ok(roadmapService.get(roadmapId));
    }

    // 지도 수정
    @PutMapping("/{roadmapId}")
    public ResponseEntity<Long> updateRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId,
        @RequestBody @Valid final RoadmapRequest request) {
//        roadmapService.update(roadmapId, request);
        return ResponseEntity.ok(roadmapId);
    }

    // 지도 삭제
    @DeleteMapping("/{roadmapId}")
    public ResponseEntity<Void> deleteRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId) {
        final ReferencedWarning referencedWarning = roadmapService.getReferencedWarning(roadmapId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        roadmapService.delete(roadmapId);
        return ResponseEntity.noContent().build();
    }
}
