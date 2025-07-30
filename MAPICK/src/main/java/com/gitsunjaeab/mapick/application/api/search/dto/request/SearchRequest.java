package com.gitsunjaeab.mapick.application.api.search.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 최신 검색어 저장 요청 DTO
 */

@Getter
@Setter
public class SearchRequest {

    @NotBlank
    private String keyword;
}
