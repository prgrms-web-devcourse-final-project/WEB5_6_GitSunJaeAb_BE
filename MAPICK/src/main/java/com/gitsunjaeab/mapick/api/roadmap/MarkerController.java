package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCreateRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "마커 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/markers", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarkerController {

    private final MarkerService markerService;

    // 마커 생성
    @PostMapping (consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "마커 생성", description = "[작성자] 레이어 위에 마커를 생성")
    public ResponseEntity<MarkerResponse> createMarker(
        @RequestPart @Valid final MarkerCreateRequest request,
        @RequestPart(required = false) MultipartFile imageFile,
        @AuthenticationPrincipal Principal principal
        ) {

        Long memberId = principal.getMember().getId();
        markerService.create(memberId, request, imageFile);
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
    @PutMapping(value = "/{markerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "마커 수정", description = "[작성자] 특정 마커를 수정")
    public ResponseEntity<MarkerResponse> updateMarker(
        @PathVariable(name = "markerId") final Long markerId,
        @RequestPart @Valid MarkerUpdateRequest request,
        @RequestPart(required = false) MultipartFile imageFile)
        {

        markerService.update(markerId, request, imageFile);
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
