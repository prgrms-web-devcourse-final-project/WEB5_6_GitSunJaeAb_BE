package com.gitsunjaeab.mapick.api.search.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCustomImageResponse;
import com.gitsunjaeab.mapick.api.search.dto.SearchHistoryDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import java.time.LocalDateTime;
import java.util.List;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<SearchHistoryDTO> SearchHistoryDTOs;

//    @JsonInclude(JsonInclude.Include.NON_NULL)


    public static SearchListResponse get(List<SearchHistoryDTO> SearchHistoryDTOs) {
        return new SearchListResponse(
                ResponseCode.OK.getCode(),
                "최근 검색어 목록 조회 완료",
                LocalDateTime.now(),
                SearchHistoryDTOs
        );
    }



}
