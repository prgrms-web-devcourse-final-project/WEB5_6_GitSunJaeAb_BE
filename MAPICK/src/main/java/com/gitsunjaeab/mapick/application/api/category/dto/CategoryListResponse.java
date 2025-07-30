package com.gitsunjaeab.mapick.application.api.category.dto;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CategoryListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<CategoryDTO> categories;

    public static CategoryListResponse get(List<CategoryDTO> categoryDTOList) {
        return new CategoryListResponse(
            ResponseCode.OK.getCode(),
            "카테고리 목록 조회 성공",
            OffsetDateTime.now(),
            categoryDTOList
        );
    }
}