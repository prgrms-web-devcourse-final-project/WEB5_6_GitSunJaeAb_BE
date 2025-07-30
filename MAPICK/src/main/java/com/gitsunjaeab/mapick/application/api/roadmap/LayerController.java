package com.gitsunjaeab.mapick.application.api.roadmap;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerDetailDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerListDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerZzimSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.request.LayerRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.request.LayerSyncRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response.LayerListResponse;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response.LayerResponse;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response.LayerZzimListResponse;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response.LayerZzimResponse;
import com.gitsunjaeab.mapick.application.roadmap.Layer.ForkResult;
import com.gitsunjaeab.mapick.application.roadmap.Layer.LayerLibraryService;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerService;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.auth.Principal;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
public class LayerController {

    private final LayerService layerService;
    private final LayerLibraryService layerLibraryService;


    // ===== 실시간 공유지도 상 레이어 CRUD =====
    @PostMapping("/sync")
    @Operation(summary = "실시간 레이어 동기화", description = "[실시간 공유지도용] 레이어 생성/수정/삭제 동기화 처리")
    public ResponseEntity<LayerResponse> syncLayer(@RequestBody LayerSyncRequest request) {
        LayerResponse response;
        switch (request.getAction()) {
            case "add" -> response = layerService.createFromSync(request);
            case "update" -> response = layerService.updateFromSync(request);
            case "delete" -> response = layerService.deleteByTempId(request.getLayerTempId());
            default -> throw new IllegalArgumentException("지원하지 않는 action: " + request.getAction());
        }

        return ResponseEntity.ok(response);
    }

    // ===== 기본 CRUD =====

    // 레이어 생성 (포크/일반)
    @PostMapping
    @Operation(summary = "[사용자]레이어 생성", description = "[사용자용] 지도 위에 레이어를 생성 또는 포크")
    public ResponseEntity<LayerResponse> createLayer(
        @AuthenticationPrincipal Principal principal,
        @RequestBody @Valid final LayerRequest request,
        @RequestParam(required = false) Long targetRoadmapId) {
        // 인증 체크
        if (principal == null) {
            throw new CommonException(ResponseCode.UNAUTHORIZED);
        }
        Long memberId = principal.getMember().getId();
        // 일반 생성: request.getOriginalLayerId() == null, 포크: originalLayerId != null
        final ForkResult result = layerService.create(request, memberId, targetRoadmapId);
        boolean isForked = (request.getOriginalLayerId() != null);
        String message = isForked ? "레이어 포크 성공" : "레이어 생성 성공";
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(LayerResponse.createFork(
                result.forkedLayer(),
                result.forkOriginLayer(),
                result.forkOriginRoadmap(),
                result.targetRoadmap(),
                isForked,
                message));
    }

    // 로드맵의 레이어 조회 (쿼리 파라미터 방식-전체/특정 조회)
    @GetMapping("/roadmap")
    @Operation(summary = "[사용자] 로드맵의 레이어 조회", description = "[사용자용] 특정 지도에 있는 레이어 전체 조회")
    public ResponseEntity<LayerListResponse> findAllLayersOnRoadmap(
        @RequestParam(required = false) Long roadmapId) {

        List<LayerListDTO> layers = layerService.findAllLayersOnRoadmap(roadmapId);
        LayerListResponse response = LayerListResponse.getList(layers);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    // 레이어 상세 조회
    @GetMapping("/{layerId}")
    @Operation(summary = "[사용자] 레이어 상세 조회", description = "[사용자용] 특정 레이어를 조회")
    public ResponseEntity<LayerResponse> findLayer(
        @AuthenticationPrincipal Principal principal,
        @PathVariable(name = "layerId") final Long layerId) {

        Long memberId = principal != null ? principal.getMember().getId() : null;

        LayerDetailDTO layerDetailDTO = layerService.getLayerDetail(layerId, memberId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(LayerResponse.get(layerDetailDTO, "레이어 상세 조회 성공"));
    }

    // 레이어 수정
    @PutMapping("/{layerId}")
    @Operation(summary = "[사용자] 레이어 수정", description = "[사용자용] 특정 레이어를 수정")
    public ResponseEntity<LayerResponse> updateLayer(
        @AuthenticationPrincipal Principal principal,
        @PathVariable(name = "layerId") final Long layerId,
        @RequestBody @Valid final LayerRequest request) {

        if (principal == null) {
            throw new CommonException(ResponseCode.UNAUTHORIZED);
        }

        Long memberId = principal.getMember().getId();
        final Layer updatedLayer = layerService.update(layerId, request, memberId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(LayerResponse.update(updatedLayer, false, "레이어 수정 성공"));
    }

    // 레이어 삭제
    @DeleteMapping("/{layerId}")
    @Operation(summary = "[사용자] 레이어 삭제", description = "[사용자용] 특정 레이어 삭제")
    public ResponseEntity<LayerResponse> deleteLayer(
        @AuthenticationPrincipal Principal principal,
        @PathVariable(name = "layerId") final Long layerId) {

        Long memberId = principal.getMember().getId();

        Layer deletedLayer = layerService.delete(layerId, memberId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(LayerResponse.delete(deletedLayer, false, "레이어 삭제 성공"));
    }

    // ===== 마이페이지 : 레이어 찜 관리  =====

    // 특정 회원의 레이어 찜 목록 조회
    @GetMapping("/member")
    @Operation(summary = "[사용자] 회원 찜 레이어 조회", description = "[사용자용] 마이페이지 > 본인이 찜한 레이어 목록 조회")
    public ResponseEntity<LayerZzimListResponse> findMemberLayers(
        @AuthenticationPrincipal Principal principal) {

        LayerZzimSimpleDTO layerZzimSimpleDTO = layerLibraryService.findAllMemberLayers(
            principal.getMember());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(LayerZzimListResponse.of(layerZzimSimpleDTO, "찜한 레이어 목록 조회 성공"));
    }

    // 레이어 찜 추가
    @PostMapping("/member")
    @Operation(summary = "[사용자] 레이어 찜 추가", description = "[사용자용] 마이페이지 > 레이어 찜 추가")
    public ResponseEntity<LayerZzimResponse> zzimLayer(
        @AuthenticationPrincipal Principal principal,
        @RequestParam Long layerId) {

        Long memberId = principal.getMember().getId();

        LayerLibrary library = layerLibraryService.addLibrary(memberId, layerId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(LayerZzimResponse.zzim(library, "레이어 찜 추가 성공"));
    }

    // 레이어 찜 해제
    @DeleteMapping("/member")
    @Operation(summary = "[사용자] 레이어 찜 해제", description = "[사용자용] 마이페이지 > 레이어 찜 취소")
    public ResponseEntity<LayerZzimResponse> removeZzimLayer(
        @AuthenticationPrincipal Principal principal,
        @RequestParam Long layerId) {

        Long memberId = principal.getMember().getId();

        LayerLibrary library = layerLibraryService.removeLibrary(memberId, layerId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(LayerZzimResponse.removeZzim(library, "레이어 찜 해제 성공"));
    }

    // 레이어 포크 (찜한 레이어를 내 로드맵에 추가)
//    @PostMapping("/member/fork")
//    @Operation(summary = "[사용자] 레이어 포크", description = "[사용자용] 마이페이지 > 찜한 레이어를 내 로드맵에 추가")
//    public ResponseEntity<LayerForkResponse> forkLayer(
//        @AuthenticationPrincipal Principal principal,
//        @RequestParam Long layerId,
//        @RequestParam Long targetRoadmapId) {
//
//        Long memberId = principal.getMember().getId();
//
//        ForkResult result = layerLibraryService.forkLayer(memberId, layerId, targetRoadmapId);
//
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(LayerForkResponse.fork(result.library(),
//                result.forkedLayer(),
//                result.targetRoadmap(),
//                "레이어 포크 성공")
//            );
//    }
}
