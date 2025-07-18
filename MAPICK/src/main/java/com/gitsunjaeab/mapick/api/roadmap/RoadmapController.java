package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapResponse;
import com.gitsunjaeab.mapick.application.roadmap.RoadmapService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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


@RestController
@RequestMapping(value = "/roadmaps", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "지도 관리 API", description = "개인지도 및 공유지도 관련 API")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(final RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    // 전체 조회 (개인로드맵, 공유지도) >> NOTE 프론트 데이터 확인용 (추후 삭제예정)
    @GetMapping
    @Operation(summary = "전체 지도(개인로드맵, 공유지도) 조회", description = "데이터 확인용 API (추후 삭제 예정), 관리자에서 쓰일 지는 미정")
    public ResponseEntity<RoadmapListResponse> getAllRoadmaps() {
        RoadmapListResponse response = roadmapService.getAllRoadmaps();
        return ResponseEntity.ok(response);
    }

    // 개인 로드맵(PERSONAL) 조회 NOTE 완
    @GetMapping("/personal")
    @Operation(summary = "개인 로드맵 조회", description = "[사용자용] 전체 로드맵을 조회하거나 카테고리별 로드맵을 조회")
    public ResponseEntity<RoadmapListResponse> getRoadmaps(
        @RequestParam(required = false) Long categoryId,
        @AuthenticationPrincipal Principal principal) {

        if (principal == null || principal.getMember() == null) {
            throw new UnauthenticatedException(ResponseCode.UNAUTHORIZED);
        }
        if (categoryId == null) {
            // 전체 조회
            return ResponseEntity.ok(roadmapService.getAllPersonalRoadmapsWithCitation(principal.getMember()));
        } else {
            // 카테고리별 조회
            return ResponseEntity.ok(roadmapService.getPersonalRoadmapsByCategory(categoryId, principal.getMember()));
        }
    }

    // 공유지도(SHARED) 조회
    @GetMapping("/shared")
    @Operation(summary = "공유지도 조회", description = "[사용자용] 전체 공유지도를 조회하거나 카테고리별 공유지도를 조회")
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

    // 특정 지도 상세 조회 NOTE 완
    @GetMapping("/{roadmapId}")
    @Operation(summary = "지도 상세 조회", description = "[사용자용] 지도 관련 속성 상세 조회")
    public ResponseEntity<RoadmapResponse> getRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId,
        @AuthenticationPrincipal Principal principal) {

        if (principal == null || principal.getMember() == null) {
            throw new UnauthenticatedException(ResponseCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(roadmapService.get(roadmapId, principal.getMember()));
    }

    // TODO 해시태그로 로드맵 검색 >> {hashtagId} 활용??

    // 개인 로드맵 생성 NOTE 완
    @PostMapping("/personal")
    @Operation(summary = "로드맵 생성", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 저장 + 해시태그 생성 ")
    public ResponseEntity<ApiResponse> createRoadmap(
            @RequestBody @Valid final RoadmapRequest request,
            @AuthenticationPrincipal final Principal principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");
        }

        Long memberId = principal.getMember().getId();
        roadmapService.create(request, memberId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "로드맵 생성 완료"));
    }

    // 공유지도 생성
    @PostMapping("/shared")
    @Operation(summary = "공유지도 생성", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 저장")
    public ResponseEntity<ApiResponse> createSharedRoadmap(@RequestBody @Valid final RoadmapRequest request) {
        // final Long createdId = roadmapService.create(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "공유지도 생성 완료"));
    }

    // 개인 로드맵 수정 NOTE 완
    @PutMapping("/personal/{roadmapId}")
    @Operation(summary = "로드맵 수정", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 수정")
    public ResponseEntity<ApiResponse> updateRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId,
        @RequestBody @Valid final RoadmapRequest request,
        @AuthenticationPrincipal final Principal principal) {

        if (principal == null || principal.getMember() == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");
        }

        Long memberId = principal.getMember().getId();
        roadmapService.update(request, roadmapId, memberId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "로드맵 수정 완료"));
    }

    // 공유지도 수정
    @PutMapping("/shared/{roadmapId}")
    @Operation(summary = "공유지도 수정", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 수정")
    public ResponseEntity<ApiResponse> updateSharedRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId,
        @RequestBody @Valid final RoadmapRequest request) {
//        roadmapService.update(roadmapId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "공유지도 수정 완료"));
    }

    // 특정 회원의 작성 개인 로드맵/공유지도 목록 조회 NOTE 완
    @GetMapping("/member")
    @Operation(summary = "회원 작성 로드맵/공유지도 목록 조회", description = "[제한적 공개] 본인이 작성한 공개/비공개 지도 목록 조회")
    public ResponseEntity<RoadmapListResponse> getMemberMaps(
            @AuthenticationPrincipal Principal principal) {

        if (principal == null || principal.getMember() == null) {
            throw new UnauthenticatedException(ResponseCode.UNAUTHORIZED);
        }

        Long memberId = principal.getMember().getId();
        return ResponseEntity.ok(roadmapService.findAllRoadmapsByMember(memberId));
    }

    // 개인 로드맵/공유지도 삭제 NOTE 완
    @DeleteMapping("/{roadmapId}")
    @Operation(summary = "로드맵/공유지도 삭제", description = "[사용자/관리자용] 생성자나 관리자가 로드맵이나 공유지도를 삭제")
    public ResponseEntity<ApiResponse> deleteRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId,
        @AuthenticationPrincipal Principal principal) {

        if (principal == null || principal.getMember() == null) {
            throw new UnauthenticatedException(ResponseCode.UNAUTHORIZED);
        }
        Member member = principal.getMember();
        final ReferencedWarning referencedWarning = roadmapService.getReferencedWarning(roadmapId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        roadmapService.delete(roadmapId, member);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "삭제 완료"));
    }
}
