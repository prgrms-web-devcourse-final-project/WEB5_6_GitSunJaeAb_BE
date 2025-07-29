package com.gitsunjaeab.mapick.api.category.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Top5CategoriesResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<CategoryDTO> categories;

    public static Top5CategoriesResponse get(List<CategoryDTO> dtoList) {
        return new Top5CategoriesResponse(
            ResponseCode.OK.getCode(),
            "상위 5개 인기 카테고리 조회 성공",
            OffsetDateTime.now(),
            dtoList
        );
    }
}
