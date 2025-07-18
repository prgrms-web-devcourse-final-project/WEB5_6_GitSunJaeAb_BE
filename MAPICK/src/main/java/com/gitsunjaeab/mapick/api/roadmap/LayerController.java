package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimResponse;
import com.gitsunjaeab.mapick.application.roadmap.LayerLibraryService;
import com.gitsunjaeab.mapick.application.roadmap.LayerService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.nio.file.AccessDeniedException;
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
@RequestMapping(value = "/layers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "레이어 관리 API")
public class LayerController {

    private final LayerService layerService;
    private final LayerLibraryService layerLibraryService;

    public LayerController(final LayerService layerService,
        LayerLibraryService layerLibraryService) {
        this.layerService = layerService;
        this.layerLibraryService = layerLibraryService;
    }


    // ===== 기본 CRUD =====

    // 레이어 생성
    @PostMapping
    @Operation(summary = "레이어 생성", description = "[사용자용] 지도 위에 레이어를 생성")
    public ResponseEntity<ApiResponse> createLayer(@RequestBody @Valid final LayerRequest request) {
        final Long createdId = layerService.create(request);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 생성 완료"));
    }

    // 로드맵의 레이어 조회 (쿼리 파라미터 방식-전체/특정 조회)
    @GetMapping("/roadmap")
    @Operation(summary = "로드맵의 레이어 조회", description = "[사용자용] 특정 지도에 있는 레이어 전체 조회")
    public ResponseEntity<LayerListResponse> getAllLayersOnRoadmap(
        @RequestParam(required = false) Long roadmapId
    ) {
        return ResponseEntity.ok(layerService.findAllLayersOnRoadmap(roadmapId));
    }

    // 레이어 상세 조회
    @GetMapping("/{layerId}")
    @Operation(summary = "레이어 상세 조회", description = "[사용자용] 특정 레이어를 조회")
    public ResponseEntity<LayerResponse> getLayer(
        @PathVariable(name = "layerId") final Long layerId,
        @AuthenticationPrincipal Principal principal
    ) {
        // 현재 로그인한 사용자의 찜여부를 서버에서 자동 판단
        Long memberId = principal != null ? principal.getMember().getId() : null;
        return ResponseEntity.ok(layerService.getLayerDetail(layerId, memberId));
    }

    // 레이어 수정
    @PutMapping("/{layerId}")
    @Operation(summary = "레이어 수정", description = "[사용자용] 특정 레이어를 수정")
    public ResponseEntity<ApiResponse> updateLayer(
        @PathVariable(name = "layerId") final Long layerId,
        @RequestBody @Valid final LayerRequest request,
        @AuthenticationPrincipal Principal principal
    ) throws AccessDeniedException {
        if (principal == null) {
            throw new IllegalStateException("인증된 유저 정보 없음");
        }
        Long memberId = principal.getMember().getId();

        layerService.update(layerId, request, memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 수정 완료"));
    }

    // 레이어 삭제
    @DeleteMapping("/{layerId}")
    @Operation(summary = "레이어 삭제", description = "[사용자용] 특정 레이어 삭제")
    public ResponseEntity<ApiResponse> deleteLayer(
        @PathVariable(name = "layerId") final Long layerId,
        @AuthenticationPrincipal Principal principal
    ) throws AccessDeniedException {
        Long memberId = principal.getMember().getId();
        return layerService.delete(layerId, memberId); // LayerService의 응답을 직접 반환
    }



    // ===== 마이페이지 : 레이어 찜 관리  =====

    // 특정 회원의 레이어 찜 목록 조회
    @GetMapping("/member")
    @Operation(summary = "회원 찜 레이어 조회", description = "[사용자용] 마이페이지 > 본인이 찜한 레이어 목록 조회")
    public ResponseEntity<LayerZzimListResponse> getMemberLayers(
        @AuthenticationPrincipal Principal principal
    ) {

        return ResponseEntity.ok(layerLibraryService.findAllMemberLayers(principal.getMember()));
    }

    // 레이어 찜 추가
    @PostMapping("/member")
    @Operation(summary = "레이어 찜 추가", description = "[사용자용] 마이페이지 > 레이어 찜 추가")
    public ResponseEntity<LayerZzimResponse> addLayerToLibrary(
        @RequestParam Long layerId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();

        LayerZzimResponse response = layerLibraryService.addLibrary(memberId, layerId);
        return ResponseEntity.ok(response);
    }

    // 레이어 찜 해제
    @DeleteMapping("/member")
    @Operation(summary = "레이어 찜 해제", description = "[사용자용] 마이페이지 > 레이어 찜 취소")
    public ResponseEntity<LayerZzimResponse> removeLayerFromLibrary(
        @RequestParam Long layerId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();
        LayerZzimResponse response = layerLibraryService.removeLibrary(memberId, layerId);

        return ResponseEntity.ok(response);
    }

    // 찜한 레이어를 내 로드맵에 포크
    @PostMapping("/member/{layerId}/fork")
    @Operation(summary = "찜한 레이어 포크", description = "[사용자용] 마이페이지 > 찜한 레이어를 내 로드맵에 포크")
    public ResponseEntity<LayerZzimResponse> forkZzimedLayer(
        @PathVariable(name = "layerId") final Long layerId,
        @RequestParam Long targetRoadmapId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();
        LayerZzimResponse response = layerLibraryService.forkLayer(memberId, layerId, targetRoadmapId);

        return ResponseEntity.ok(response);
    }



    // ===== 안 쓰이는데 만든 기능.... =====

    // 전체 회원 라이브러리 레이어 조회
//    @GetMapping("/library")
//    @Operation(summary = "레이어 라이브러리 조회", description = "[사용자용] 전체 회원 레이어 목록 조회")
//    public ResponseEntity<LayerListResponse> findAllLibraryLayers() {
//        return ResponseEntity.ok(layerLibraryService.findAllLibraryLayers());
//    }

}
