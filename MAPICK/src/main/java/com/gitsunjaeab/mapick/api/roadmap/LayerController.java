package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.member.dto.MemberLayersResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerListResponse;
import com.gitsunjaeab.mapick.application.roadmap.LayerService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/layers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "레이어 관리 API")
public class LayerController {

    private final LayerService layerService;

    public LayerController(final LayerService layerService) {
        this.layerService = layerService;
    }

    // 특정 회원의 레이어 찜 목록 조회 >> LayerLibrary 에서 모아둔 레이어 조회
    @GetMapping("/member")
    @Operation(summary = "회원 찜 레이어 조회", description = "[사용자용] 마이페이지 > 본인이 찜한 레이어 목록 조회")
    public ResponseEntity<LayerListResponse> getMemberLayers(
        @RequestParam(required = false) Long memberId) {

        return ResponseEntity.ok(layerService.findAllMemberLayers(memberId));
    }

    // TODO 사용자 >> 레이어 찜하기


    // 특정 지도에 적용된 레이어 목록 조회
    @GetMapping("/roadmap")
    @Operation(summary = "지도에 적용된 레이어 목록 조회", description = "[사용자용] 특정 지도에 있는 레이어 전체 조회")
    public ResponseEntity<LayerListResponse> getAllLayersOnRoadmap(
        @RequestParam(required = false) Long roadmapId
    ) {
        return ResponseEntity.ok(layerService.findAllLayersOnRoadmap(roadmapId));
    }

    // 레이어 생성
    @PostMapping
    @Operation(summary = "레이어 생성", description = "[사용자용] 지도 위에 레이어를 생성")
    public ResponseEntity<ApiResponse> createLayer(@RequestBody @Valid final LayerRequest request) {
//        final Long createdId = layerService.create(layerDTO);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 생성 완료"));
    }

    // 특정 레이어 수정
    @PutMapping("/{layerId}")
    @Operation(summary = "레이어 수정", description = "[사용자용] 특정 레이어를 수정")
    public ResponseEntity<ApiResponse> updateLayer(@PathVariable(name = "layerId") final Long layerId,
            @RequestBody @Valid final LayerRequest request) {
//        layerService.update(layerId, layerDTO);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 수정 완료"));
    }

    // 특정 레이어 삭제
    @DeleteMapping("/{layerId}")
    @Operation(summary = "레이어 삭제", description = "[사용자용] 특정 레이어 삭제")
    public ResponseEntity<ApiResponse> deleteLayer(@PathVariable(name = "layerId") final Long layerId) {
//        final ReferencedWarning referencedWarning = layerService.getReferencedWarning(layerId);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        layerService.delete(layerId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 삭제 완료"));
    }

    // 특정 레이어 조회 >> 이게 필요한가...?
//    @GetMapping("/{layerId}")
//    public ResponseEntity<LayerDTO> getLayer(@PathVariable(name = "layerId") final Long layerId) {
//        return ResponseEntity.ok(layerService.get(layerId));
//    }

}
