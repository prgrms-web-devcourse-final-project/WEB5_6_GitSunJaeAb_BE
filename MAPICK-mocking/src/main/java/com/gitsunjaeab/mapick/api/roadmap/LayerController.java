package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.application.roadmap.LayerService;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerDTO;
import com.gitsunjaeab.mapick.application.roadmap.LayerLibraryService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/layers", produces = MediaType.APPLICATION_JSON_VALUE)
public class LayerController {

    private final LayerService layerService;

    public LayerController(final LayerService layerService) {
        this.layerService = layerService;
    }

    // TODO 특정 회원의 레이어 조회 >> LayerLibrary 에서 모아둔 레이어 조회
//    @GetMapping("/")
//    public ResponseEntity<List<LayerLibraryDTO>> getAllLayerLibraries() {
//        return ResponseEntity.ok(layerLibraryService.findAll());
//    }

    // TODO 레이어 조회 (지도에 적용되어 있는, 수정이나 상제보기 시에 조회할 레이어)

    // 레이어 생성
    @PostMapping
    public ResponseEntity<ApiResponse> createLayer(@RequestBody @Valid final LayerRequest request) {
//        final Long createdId = layerService.create(layerDTO);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 생성 완료"));
    }

    // 특정 레이어 수정
    @PutMapping("/{layerId}")
    public ResponseEntity<ApiResponse> updateLayer(@PathVariable(name = "layerId") final Long layerId,
            @RequestBody @Valid final LayerRequest request) {
//        layerService.update(layerId, layerDTO);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 수정 완료"));
    }

    // 특정 레이어 삭제
    @DeleteMapping("/{layerId}")
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
