package com.gitsunjaeab.mapick.api.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoryDTO categoryDTO;

    public static CategoryResponse create() {
        return new CategoryResponse(
            ResponseCode.OK.getCode(),
            "카테고리 생성 완료",
            OffsetDateTime.now(),
            null
        );
    }

    public static CategoryResponse update() {
        return new CategoryResponse(
            ResponseCode.OK.getCode(),
            "카테고리 수정 완료",
            OffsetDateTime.now(),
            null
        );
    }

    public static CategoryResponse delete() {
        return new CategoryResponse(
            ResponseCode.OK.getCode(),
            "카테고리 삭제 완료",
            OffsetDateTime.now(),
            null
        );
    }
}
