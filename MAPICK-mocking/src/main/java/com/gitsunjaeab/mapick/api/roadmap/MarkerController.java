package com.gitsunjaeab.mapick.api.roadmap;

import ch.qos.logback.classic.boolex.MarkerList;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerRequest;
import com.gitsunjaeab.mapick.application.roadmap.MarkerService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/markers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "마커 관리 API")
public class MarkerController {

    private final MarkerService markerService;
    private final MemberRepository memberRepository;
    private final LayerRepository layerRepository;

    public MarkerController(final MarkerService markerService,
            final MemberRepository memberRepository, final LayerRepository layerRepository) {
        this.markerService = markerService;
        this.memberRepository = memberRepository;
        this.layerRepository = layerRepository;
    }

    // 특정 레이어에 적용된 마커 목록 조회
    @GetMapping
    @Operation(summary = "마커 목록 조회", description = "[사용자용] 특정 레이어에 놓인 마커 전체 조회")
    public ResponseEntity<MarkerListResponse> getAllMarkersOnLayer(
        @RequestParam(required = false) Long layerId
    ) {
        return ResponseEntity.ok(markerService.findAllMarkersOnLayer(layerId));
    }

    // 마커 생성
    @PostMapping
    @Operation(summary = "마커 생성", description = "[사용자용] 레이어 위에 마커를 생성")
    public ResponseEntity<ApiResponse> createMarker(@RequestBody @Valid final MarkerRequest request) {
//        final Long createdId = markerService.create(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "마커 생성 완료"));
    }

    // 마커 수정
    @PutMapping("/{markerId}")
    @Operation(summary = "마커 수정", description = "[사용자용] 특정 마커를 수정")
    public ResponseEntity<ApiResponse> updateMarker(@PathVariable(name = "markerId") final Long markerId,
            @RequestBody @Valid final MarkerRequest request) {
//        markerService.update(markerId, markerDTO);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "마커 수정 완료"));
    }

    // 마커 삭제
    @DeleteMapping("/{markerId}")
    @Operation(summary = "마커 삭제", description = "[사용자용] 특정 마커를 삭제")
    public ResponseEntity<ApiResponse> deleteMarker(@PathVariable(name = "markerId") final Long markerId) {
//        final ReferencedWarning referencedWarning = markerService.getReferencedWarning(markersId);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        markerService.delete(markersId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "마커 삭제 완료"));
    }

//    특정 마커 조회 >> 어차피 전체 마커를 조회해오면 이게 필요한가?
//    @GetMapping("/{markerId}")
//    public ResponseEntity<MarkerDTO> getMarker(@PathVariable(name = "markerId") final Long markerId) {
//        return ResponseEntity.ok(markerService.get(markerId));
//    }
}
