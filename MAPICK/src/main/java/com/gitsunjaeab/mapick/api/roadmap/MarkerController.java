package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.*;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ===== 실시간 공유지도 상 레이어 CRUD =====
    @Operation(summary = "마커 동기화", description = "[실시간 공유지도용] 마커 생성/수정/삭제 처리")
    @PostMapping("/sync")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MarkerResponse> syncMarker(@RequestBody MarkerSyncRequest request) {
        Marker marker;
        switch (request.getAction()) {
            case "add" -> marker = markerService.createFromSync(request);
            case "update" -> marker = markerService.updateFromSync(request);
            case "delete" -> marker = markerService.deleteFromSync(request);
            default -> throw new IllegalArgumentException("지원하지 않는 action: " + request.getAction());
        }

        return ResponseEntity.ok(MarkerResponse.of(marker, "마커 " + request.getAction() + " 성공"));

    }

    /**
     * 관리자 커스텀 이미지 마커
     */

    @Operation(summary = "커스텀 마커 이미지 등록", description = "[관리자] 커스텀 마커 이미지를 등록")
    @PostMapping(value = "/customImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MarkerCustomImageResponse> createCustomImage(
        @RequestParam final String name,
        @RequestParam(required = false) final MultipartFile imageFile
    ) {

        markerService.createCustomImage(name, imageFile);
        final MarkerCustomImageResponse response = MarkerCustomImageResponse.create();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "커스텀 마커 이미지 목록", description = "[관리자/사용자] 커스텀 마커 이미지 전체 조회")
    @GetMapping("/customImages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<MarkerCustomImageResponse> getCustomImageList()
    {
        List<MarkerCustomImageDTO> customImageDTOS = markerService.getCustomImages();
        MarkerCustomImageResponse response = MarkerCustomImageResponse.getList(customImageDTOS);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "커스텀 마커 이미지 수정", description = "[관리자] 커스텀 마커 이미지를 수정")
    @PutMapping(value = "/customImage/{customImageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MarkerCustomImageResponse> updateCustomImage(
        @PathVariable(name = "customImageId") final Long customImageId,
        @RequestParam(required = false) final String name,
        @RequestParam(required = false) final MultipartFile imageFile
    ) {

        markerService.updateCustomImage(customImageId, name, imageFile);
        final MarkerCustomImageResponse response = MarkerCustomImageResponse.update();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "커스텀 마커 이미지 삭제", description = "[관리자] 커스텀 마커 이미지를 삭제")
    @DeleteMapping("/customImage/{customImageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MarkerCustomImageResponse> deleteCustomImage(
        @PathVariable(name = "customImageId") final Long customImageId) {

        markerService.deleteCustomImage(customImageId);
        final MarkerCustomImageResponse response = MarkerCustomImageResponse.delete();

        return ResponseEntity.ok(response);
    }


    /**
     * 로드맵 위의 마커
     */

    @Operation(summary = "마커 생성", description = "[작성자] 레이어 위에 마커를 생성")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MarkerResponse> createMarker(
        @RequestBody @Valid final MarkerCreateRequest request,
        @AuthenticationPrincipal Principal principal
        ) {

        final Long memberId = principal.getMember().getId();
        markerService.create(memberId, request);
        final MarkerResponse response = MarkerResponse.create();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 마커 조회", description = "마커 수정 시, 특정 마커에 대한 정보 조회 필요")
    @GetMapping("/{markerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MarkerResponse> getMarker(
        @PathVariable(name = "markerId") final Long markerId) {

        final MarkerDTO dto = markerService.get(markerId);
        final MarkerResponse response = MarkerResponse.get(dto);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "마커 수정", description = "[작성자] 특정 마커를 수정")
    @PutMapping("/{markerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MarkerResponse> updateMarker(
        @PathVariable(name = "markerId") final Long markerId,
        @RequestBody @Valid final MarkerUpdateRequest request)
        {

        markerService.update(markerId, request);
        final MarkerResponse response = MarkerResponse.update();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "마커 삭제", description = "[작성자] 특정 마커를 삭제")
    @DeleteMapping("/{markerId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MarkerResponse> deleteMarker(
        @PathVariable(name = "markerId") final Long markerId) {

        markerService.delete(markerId);
        final MarkerResponse response = MarkerResponse.delete();

        return ResponseEntity.ok(response);
    }
}
