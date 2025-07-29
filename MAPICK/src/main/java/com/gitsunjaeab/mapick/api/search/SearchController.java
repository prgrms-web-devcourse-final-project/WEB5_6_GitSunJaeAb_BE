package com.gitsunjaeab.mapick.api.search;

import com.gitsunjaeab.mapick.api.search.dto.internal.SearchHistoryDTO;
import com.gitsunjaeab.mapick.api.search.dto.request.SearchRequest;
import com.gitsunjaeab.mapick.api.search.dto.response.SearchResponse;
import com.gitsunjaeab.mapick.application.search.SearchHistoryService;
import com.gitsunjaeab.mapick.application.search.SearchService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

    private final SearchService searchService;
    private final SearchHistoryService searchHistoryService;

//    @GetMapping
//    public ResponseEntity<SearchListResponse> search(@RequestParam("keyword") String keyword) {
//        SearchListResponse resultResponse = searchService.search(keyword);
//
//        return ResponseEntity.ok(resultResponse);
//    }


    // 최신 검색 목록 조회
    // complete
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "최신 검색 목록 조회", description = "최신 검색 목록 조회" )
    public ResponseEntity<SearchResponse> getSearchHistories(
            @AuthenticationPrincipal Principal principal) {

        Long memberId = principal.getMember().getId();

        List<SearchHistoryDTO> searchHistoryDTOs = searchHistoryService.getSearchHistories(memberId);

        SearchResponse response = SearchResponse.getList(searchHistoryDTOs);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 최신 검색 목록 저장
    // complete
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "최신 검색 목록 저장", description = "최신 검색 목록 저장" )
    public ResponseEntity<SearchResponse> saveSearchHistory(
            @AuthenticationPrincipal Principal principal,
            @RequestBody SearchRequest searchRequest) {

        Long memberId = principal.getMember().getId();

        searchHistoryService.saveSearchHistory(memberId,searchRequest);

        SearchResponse response = SearchResponse.save();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 최신 검색 항목 삭제
    // complete
    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "최신 검색 항목 단일 삭제", description = "최신 검색 항목 단일 삭제" )
    public ResponseEntity<SearchResponse> deletSearchHistory(
            @AuthenticationPrincipal Principal principal,
            @RequestParam("keyword") String keyword) {

        Long memberId = principal.getMember().getId();

        searchHistoryService.deleteSearchHistory(memberId,keyword);

        SearchResponse response = SearchResponse.remove();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    // 최신 검색 목록 전체 삭제
    // complete
    @DeleteMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "최신 검색 목록 삭제", description = "최신 검색 목록 삭제" )
    public ResponseEntity<SearchResponse> deletSearchHistoryList(
            @AuthenticationPrincipal Principal principal) {

        Long memberId = principal.getMember().getId();

        searchHistoryService.deleteSearchHistoryList(memberId);

        SearchResponse response = SearchResponse.removeList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }



}
