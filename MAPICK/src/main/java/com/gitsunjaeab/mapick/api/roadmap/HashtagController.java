package com.gitsunjaeab.mapick.api.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.application.roadmap.HashtagService;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagDTO;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hashtags", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "해시태그 관리 API")
public class HashtagController {

    private final HashtagService hashtagService;

    public HashtagController(final HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    // 특정 지도에 적용된 해시태그 조회
    @GetMapping("/roadmap")
    @Operation(summary = "해시태그 목록 조회", description = "[사용자용] 특정 지도에 있는 해시테그 전체 조회")
    public ResponseEntity<HashtagListResponse> getAllHashtagsOnRoadmap(
        @RequestParam(required = false) Long roadmapId
    ) {
        return ResponseEntity.ok(hashtagService.findAllHashtagsOnRoadmap(roadmapId));
    }

    // 해시태그 생성
    @PostMapping
    @Operation(summary = "해시태그 생성", description = "[사용자용] 지도 위에 해시태그를 생성")
    public ResponseEntity<ApiResponse> createHashtag(@RequestBody @Valid final HashtagRequest request) {
//        hashtagService.create(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "해시태그 생성 완료"));
    }

    // 해시태그 삭제
    @DeleteMapping("/{hashtagId}")
    @Operation(summary = "해시태그 삭제", description = "[사용자용] 특정 해시태그 삭제")
    public ResponseEntity<ApiResponse> deleteHashtag(@PathVariable(name = "hashtagId") final Long hashtagId) {
//        final ReferencedWarning referencedWarning = hashtagService.getReferencedWarning(id);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        hashtagService.delete(hashtagId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "해시태그 삭제 완료"));
    }

}
