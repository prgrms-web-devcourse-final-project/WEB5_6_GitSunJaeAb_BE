package com.gitsunjaeab.mapick.api.search;

import com.gitsunjaeab.mapick.api.search.dto.SearchResultResponse;
import com.gitsunjaeab.mapick.application.search.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResultResponse> search(@RequestParam("keyword") String keyword) {
        SearchResultResponse resultResponse = searchService.search(keyword);

        return ResponseEntity.ok(resultResponse);
    }

}
