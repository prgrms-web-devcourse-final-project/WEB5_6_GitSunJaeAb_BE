package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.*;
import com.gitsunjaeab.mapick.application.roadmap.RoadmapEditorService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/roadmaps", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "지도 관리 API", description = "개인지도 및 공유지도 관련 API")
public class RoadmapController {

    private final RoadmapService roadmapService;
    private final RoadmapEditorService roadmapEditorService;

    public RoadmapController(final RoadmapService roadmapService, final RoadmapEditorService roadmapEditorService) {
        this.roadmapService = roadmapService;
        this.roadmapEditorService = roadmapEditorService;
    }

    @GetMapping("/{roadmapId}/editors")
    @Operation(summary = "실시간 공유지도 참여자 조회", description = " [사용자 및 관리자 용] 사용자 정보 및 참여자 인원 수 조회")
    public ResponseEntity<RoadmapEditorListResponse> getEditors(@PathVariable Long roadmapId) {
        List<RoadmapEditorSimpleDTO> editors = roadmapEditorService.getRoadmapEditors(roadmapId);
        long count = roadmapEditorService.countRoadmapEditors(roadmapId);
        return ResponseEntity.ok(new RoadmapEditorListResponse(editors, count));
    }

    // 전체 조회 (개인로드맵, 공유지도) >> NOTE 프론트 데이터 확인용 (추후 삭제예정)
    @GetMapping
    @Operation(summary = "전체 지도(개인로드맵, 공유지도) 조회", description = "데이터 확인용 API (추후 삭제 예정), 관리자에서 쓰일 지는 미정")
    public ResponseEntity<RoadmapListResponse> getAllRoadmaps() {
        RoadmapListResponse response = roadmapService.getAllRoadmaps();
        return ResponseEntity.ok(response);
    }

    // 공유지도(SHARED) 조회 NOTE 완
    @GetMapping("/shared")
    @Operation(summary = "공유지도 조회", description = "[사용자용] 전체 공유지도를 조회하거나 카테고리별 공유지도를 조회")
    public ResponseEntity<RoadmapListResponse> getSharedRoadmaps(
            @RequestParam(required = false) Long categoryId,
            @AuthenticationPrincipal Principal principal) {

        if (principal == null || principal.getMember() == null) {
            throw new UnauthenticatedException(ResponseCode.UNAUTHORIZED);
        }
        if (categoryId == null) {
            // 전체 조회
            return ResponseEntity.ok(roadmapService.getAllSharedRoadmapsWithCitation());
        } else {
            // 카테고리별 조회
            return ResponseEntity.ok(roadmapService.getSharedRoadmapsByCategory(categoryId));
        }
    }

    // 공유지도 생성 NOTE 임시 완성
    @PostMapping(value = "/shared", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "공유지도 생성", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 저장")
    public ResponseEntity<RoadmapCreateResponse> createSharedRoadmap(
            @RequestPart @Valid final SharedRoadmapCreateRequest request,
            @RequestPart(required = false) MultipartFile imageFile,
            @AuthenticationPrincipal Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");
        }

        Long memberId = principal.getMember().getId();
        Long roadmapId = roadmapService.createSharedRoadmap(request, memberId, imageFile);
        return ResponseEntity.ok(RoadmapCreateResponse.of(ResponseCode.OK, "공유지도 생성 완료", roadmapId));
    }

    // 공유지도 수정
    @PutMapping(value = "/shared/{roadmapId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "공유지도 수정", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 수정")
    public ResponseEntity<SharedRoadmapUpdateResponse> updateSharedRoadmap(
            @PathVariable(name = "roadmapId") final Long roadmapId,
            @RequestPart @Valid final SharedRoadmapUpdateRequest request,
            @RequestPart(required = false) MultipartFile imageFile,
            @AuthenticationPrincipal Principal principal) {

        if (principal == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");
        }

        Long memberId = principal.getMember().getId();
        roadmapService.updateSharedRoadmap(request, roadmapId, memberId, imageFile);
        return ResponseEntity.ok(SharedRoadmapUpdateResponse.of(ResponseCode.OK, "공유지도 수정 완료"));
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
    @PostMapping(value = "/personal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "로드맵 생성", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 저장 + 해시태그 생성 ")
    public ResponseEntity<RoadmapCreateResponse> createRoadmap(
            @RequestPart @Valid final RoadmapRequest request,
            @RequestPart(required = false) MultipartFile imageFile,
            @AuthenticationPrincipal final Principal principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");
        }

        Long memberId = principal.getMember().getId();
        RoadmapAchievementResponse response = roadmapService.createRoadmap(request, memberId, imageFile);

        if (response.isAchievementUnlocked()) {
            return ResponseEntity.ok(RoadmapCreateResponse.of(
                ResponseCode.OK,
                "로드맵 생성 완료! 업적 '" + response.getAchievement().getName() + "' 을(를) 획득했습니다.",
                    response.getRoadmapId()
            ));
        }

        return ResponseEntity.ok(RoadmapCreateResponse.of(ResponseCode.OK, "로드맵 생성 완료", response.getRoadmapId()));
    }

    // 개인 로드맵 수정 NOTE 완
    @PutMapping(value = "/personal/{roadmapId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "로드맵 수정", description = "[사용자용] 레이어, 마커 제외 지도 관련 속성만 수정")
    public ResponseEntity<ApiResponse> updateRoadmap(
        @PathVariable(name = "roadmapId") final Long roadmapId,
        @RequestPart @Valid final RoadmapRequest request,
        @RequestPart(required = false) MultipartFile imageFile,
        @AuthenticationPrincipal final Principal principal) {

        if (principal == null || principal.getMember() == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");
        }

        Long memberId = principal.getMember().getId();
        roadmapService.updateRoadmap(request, roadmapId, memberId, imageFile);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "로드맵 수정 완료"));
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
