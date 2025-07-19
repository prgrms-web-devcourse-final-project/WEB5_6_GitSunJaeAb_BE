package com.gitsunjaeab.mapick.api.category.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.category.Category;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class CategoryListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 목록 조회용
    @Size(max = 255)
    private List<CategoryListItem> categories;

    public CategoryListResponse(String code, String message, LocalDateTime timestamp,
        List<CategoryListItem> categories) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.categories = categories;
    }

    // 데이터를 같이 반환하는 경우
    public static CategoryListResponse of(List<Category> categories) {
        List<CategoryListItem> list = categories.stream()
            .map(c -> new CategoryListItem(
                c.getId(),
                c.getName(),
                c.getCategoryImage(),
                c.getDescription(),
                c.getCreatedAt()
            ))
            .collect(Collectors.toList());

        return new CategoryListResponse(
            ResponseCode.OK.getCode(),
            "카테고리 조회 성공",
            LocalDateTime.now(),
            list
        );
    }

    @Getter
    @AllArgsConstructor
    public static class CategoryListItem {

        private Long id;
        private String name;
        private String categoryImage;
        private String description;
        private OffsetDateTime createdAt;
    }
}