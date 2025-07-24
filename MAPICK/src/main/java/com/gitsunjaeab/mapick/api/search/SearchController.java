package com.gitsunjaeab.mapick.api.search;

import com.gitsunjaeab.mapick.api.search.dto.SearchHistoryDTO;
import com.gitsunjaeab.mapick.api.search.dto.request.SearchRequest;
import com.gitsunjaeab.mapick.api.search.dto.response.SearchListResponse;
import com.gitsunjaeab.mapick.application.search.SearchHistoryService;
import com.gitsunjaeab.mapick.application.search.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

    private final SearchService searchService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping
    public ResponseEntity<SearchListResponse> search(@RequestParam("keyword") String keyword) {
        SearchListResponse resultResponse = searchService.search(keyword);

        return ResponseEntity.ok(resultResponse);
    }



    // 최신 검색 목록 조회
    @GetMapping("/list")
    @Operation(summary = "최신 검색 목록 조회", description = "최신 검색 목록 조회" )
    public ResponseEntity<SearchListResponse> getSearchHistories() {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        List<SearchHistoryDTO> searchHistoryDTOs = searchHistoryService.getSearchHistories(memberId);

        SearchListResponse response = SearchListResponse.get(searchHistoryDTOs);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 최신 검색 목록 저장
    @PostMapping
    @Operation(summary = "최신 검색 목록 저장", description = "최신 검색 목록 저장" )
    public ResponseEntity<SearchListResponse> saveSearchHistory(@RequestBody SearchRequest searchRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        searchHistoryService.saveSearchHistory(memberId,searchRequest);

        SearchListResponse response = SearchListResponse.save();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 최신 검색 목록 삭제


    // 최신 검색 목록 전체 삭제




}
