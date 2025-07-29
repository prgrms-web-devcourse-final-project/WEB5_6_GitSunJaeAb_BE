package com.gitsunjaeab.mapick.api.search.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.api.search.dto.internal.SearchHistoryDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import java.time.OffsetDateTime;
import java.util.List;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SearchHistoryDTO> SearchHistoryDTOs;




    public static SearchResponse getList(List<SearchHistoryDTO> SearchHistoryDTOs) {
        return new SearchResponse(
                ResponseCode.OK.getCode(),
                "최근 검색어 목록 조회 완료",
                OffsetDateTime.now(),
                SearchHistoryDTOs
        );
    }

    public static SearchResponse save() {
        return new SearchResponse(
                ResponseCode.OK.getCode(),
                "최근 검색어 저장 완료",
                OffsetDateTime.now(),
                null
        );
    }

    public static SearchResponse remove() {
        return new SearchResponse(
                ResponseCode.OK.getCode(),
                "최근 검색어 삭제 완료",
                OffsetDateTime.now(),
                null
        );
    }

    public static SearchResponse removeList() {
        return new SearchResponse(
                ResponseCode.OK.getCode(),
                "최근 검색어 목록 삭제 완료",
                OffsetDateTime.now(),
                null
        );
    }



}
