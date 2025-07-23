package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCreateRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCustomImageDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCustomImageResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "마커 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/markers", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarkerController {

    private final MarkerService markerService;

    /**
     * 관리자 커스텀 이미지 마커
     */

    // 커스텀 마커 이미지 등록
    @PostMapping(value = "/customImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "커스텀 마커 이미지 등록", description = "[관리자] 커스텀 마커 이미지를 등록")
    public ResponseEntity<MarkerCustomImageResponse> createCustomImage(
        @RequestParam String name,
        @RequestParam(required = false) MultipartFile imageFile
    ) {

        markerService.createCustomImage(name, imageFile);
        MarkerCustomImageResponse response = MarkerCustomImageResponse.create();

        return ResponseEntity.ok(response);
    }

    // 전체 커스텀 마커 이미지 조회
    @GetMapping("/customImages")
    @Operation(summary = "커스텀 마커 이미지 목록", description = "[관리자] 커스텀 마커 이미지 전체 조회")
    public ResponseEntity<MarkerCustomImageResponse> getCustomImageList()
    {
        List<MarkerCustomImageDTO> customImageDTOS = markerService.getCustomImages();
        MarkerCustomImageResponse response = MarkerCustomImageResponse.getList(customImageDTOS);

        return ResponseEntity.ok(response);
    }

    // 커스텀 마커 이미지 수정
    @PutMapping(value = "/customImage/{customImageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "커스텀 마커 이미지 수정", description = "[관리자] 커스텀 마커 이미지를 수정")
    public ResponseEntity<MarkerCustomImageResponse> updateCustomImage(
        @PathVariable(name = "customImageId") final Long customImageId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) MultipartFile imageFile
    ) {

        markerService.updateCustomImage(customImageId, name, imageFile);
        MarkerCustomImageResponse response = MarkerCustomImageResponse.update();

        return ResponseEntity.ok(response);
    }

    // 커스텀 마커 이미지 삭제
    @DeleteMapping("/customImage/{customImageId}")
    @Operation(summary = "커스텀 마커 이미지 삭제", description = "[관리자] 커스텀 마커 이미지를 삭제")
    public ResponseEntity<MarkerCustomImageResponse> deleteCustomImage(
        @PathVariable(name = "customImageId") final Long customImageId) {

        markerService.deleteCustomImage(customImageId);
        MarkerCustomImageResponse response = MarkerCustomImageResponse.delete();

        return ResponseEntity.ok(response);
    }


    /**
     * 로드맵 위의 마커
     */

    // 마커 생성
    @PostMapping
    @Operation(summary = "마커 생성", description = "[작성자] 레이어 위에 마커를 생성")
    public ResponseEntity<MarkerResponse> createMarker(
        @RequestBody @Valid final MarkerCreateRequest request,
        @AuthenticationPrincipal Principal principal
        ) {

        Long memberId = principal.getMember().getId();
        markerService.create(memberId, request);
        MarkerResponse response = MarkerResponse.create();

        return ResponseEntity.ok(response);
    }

    // 특정 마커 조회
    @GetMapping("/{markerId}")
    @Operation(summary = "특정 마커 조회", description = "마커 수정 시, 특정 마커에 대한 정보 조회 필요")
    public ResponseEntity<MarkerResponse> getMarker(
        @PathVariable(name = "markerId") final Long markerId) {

        MarkerDTO dto = markerService.get(markerId);
        MarkerResponse response = MarkerResponse.get(dto);

        return ResponseEntity.ok(response);
    }

    // 마커 수정
    @PutMapping("/{markerId}")
    @Operation(summary = "마커 수정", description = "[작성자] 특정 마커를 수정")
    public ResponseEntity<MarkerResponse> updateMarker(
        @PathVariable(name = "markerId") final Long markerId,
        @RequestBody @Valid MarkerUpdateRequest request)
        {

        markerService.update(markerId, request);
        MarkerResponse response = MarkerResponse.update();

        return ResponseEntity.ok(response);
    }

    // 마커 삭제
    @DeleteMapping("/{markerId}")
    @Operation(summary = "마커 삭제", description = "[작성자] 특정 마커를 삭제")
    public ResponseEntity<MarkerResponse> deleteMarker(
        @PathVariable(name = "markerId") final Long markerId) {

        markerService.delete(markerId);
        MarkerResponse response = MarkerResponse.delete();

        return ResponseEntity.ok(response);
    }
}
