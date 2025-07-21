package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimResponse;
import com.gitsunjaeab.mapick.application.roadmap.LayerLibraryService;
import com.gitsunjaeab.mapick.application.roadmap.LayerService;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerDetailDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimSimpleDTO;

import java.util.List;


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
    public ResponseEntity<LayerResponse> createLayer(
        @RequestBody @Valid final LayerRequest request,
        @RequestParam Long roadmapId,
        @AuthenticationPrincipal Principal principal
    ) {
        if (principal == null) {
            throw new CommonException(ResponseCode.UNAUTHORIZED);
        }
        Long memberId = principal.getMember().getId();
        
        final Layer createdLayer = layerService.create(request, memberId, roadmapId);
        
        return ResponseEntity.ok(LayerResponse.of(createdLayer, false, "레이어 생성 성공"));
    }

    // 로드맵의 레이어 조회 (쿼리 파라미터 방식-전체/특정 조회)
    @GetMapping("/roadmap")
    @Operation(summary = "로드맵의 레이어 조회", description = "[사용자용] 특정 지도에 있는 레이어 전체 조회")
    public ResponseEntity<LayerListResponse> getAllLayersOnRoadmap(
        @RequestParam(required = false) Long roadmapId
    ) {
        final List<Layer> layers = layerService.findAllLayersOnRoadmap(roadmapId);
        
        return ResponseEntity.ok(LayerListResponse.of(layers, "레이어 목록 조회 성공"));
    }

    // 레이어 상세 조회
    @GetMapping("/{layerId}")
    @Operation(summary = "레이어 상세 조회", description = "[사용자용] 특정 레이어를 조회")
    public ResponseEntity<LayerResponse> getLayer(
        @PathVariable(name = "layerId") final Long layerId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal != null ? principal.getMember().getId() : null;
        LayerDetailDTO layerDetailDTO = layerService.getLayerDetail(layerId, memberId);
        
        return ResponseEntity.ok(LayerResponse.of(layerDetailDTO, "레이어 상세 조회 성공"));
    }

    // 레이어 수정
    @PutMapping("/{layerId}")
    @Operation(summary = "레이어 수정", description = "[사용자용] 특정 레이어를 수정")
    public ResponseEntity<LayerResponse> updateLayer(
        @PathVariable(name = "layerId") final Long layerId,
        @RequestBody @Valid final LayerRequest request,
        @AuthenticationPrincipal Principal principal
    ) {
        if (principal == null) {
            throw new CommonException(ResponseCode.UNAUTHORIZED);
        }
        Long memberId = principal.getMember().getId();
        final Layer updatedLayer = layerService.update(layerId, request, memberId);

        return ResponseEntity.ok(LayerResponse.of(updatedLayer, false, "레이어 수정 성공"));
    }

    // 레이어 삭제
    @DeleteMapping("/{layerId}")
    @Operation(summary = "레이어 삭제", description = "[사용자용] 특정 레이어 삭제")
    public ResponseEntity<LayerResponse> deleteLayer(
        @PathVariable(name = "layerId") final Long layerId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();
        Layer deletedLayer = layerService.delete(layerId, memberId);
        
        return ResponseEntity.ok(LayerResponse.of(deletedLayer, false, "레이어 삭제 성공"));
    }



    // ===== 마이페이지 : 레이어 찜 관리  =====

    // 특정 회원의 레이어 찜 목록 조회
    @GetMapping("/member")
    @Operation(summary = "회원 찜 레이어 조회", description = "[사용자용] 마이페이지 > 본인이 찜한 레이어 목록 조회")
    public ResponseEntity<LayerZzimListResponse> getMemberLayers(
        @AuthenticationPrincipal Principal principal
    ) {
        LayerZzimSimpleDTO layerZzimSimpleDTO = layerLibraryService.findAllMemberLayers(principal.getMember());
        
        return ResponseEntity.ok(LayerZzimListResponse.of(layerZzimSimpleDTO, "찜한 레이어 목록 조회 성공"));
    }

    // 레이어 찜 추가
    @PostMapping("/member")
    @Operation(summary = "레이어 찜 추가", description = "[사용자용] 마이페이지 > 레이어 찜 추가")
    public ResponseEntity<LayerZzimResponse> addLayerToLibrary(
        @RequestParam Long layerId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();
        LayerLibrary library = layerLibraryService.addLibrary(memberId, layerId);
        
        return ResponseEntity.ok(LayerZzimResponse.of(library, "레이어 찜 추가 성공"));
    }

    // 레이어 찜 해제
    @DeleteMapping("/member")
    @Operation(summary = "레이어 찜 해제", description = "[사용자용] 마이페이지 > 레이어 찜 취소")
    public ResponseEntity<LayerZzimResponse> removeLayerFromLibrary(
        @RequestParam Long layerId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();
        LayerLibrary library = layerLibraryService.removeLibrary(memberId, layerId);
        
        return ResponseEntity.ok(LayerZzimResponse.of(library, "레이어 찜 해제 성공"));
    }

    // 레이어 포크 (찜한 레이어를 내 로드맵에 추가)
    @PostMapping("/member/fork")
    @Operation(summary = "레이어 포크", description = "[사용자용] 마이페이지 > 찜한 레이어를 내 로드맵에 추가")
    public ResponseEntity<LayerZzimResponse> forkLayer(
        @RequestParam Long layerId,
        @RequestParam Long targetRoadmapId,
        @AuthenticationPrincipal Principal principal
    ) {
        Long memberId = principal.getMember().getId();
        LayerLibrary library = layerLibraryService.forkLayer(memberId, layerId, targetRoadmapId);
        
        return ResponseEntity.ok(LayerZzimResponse.of(library, "레이어 포크 성공"));
    }
}
