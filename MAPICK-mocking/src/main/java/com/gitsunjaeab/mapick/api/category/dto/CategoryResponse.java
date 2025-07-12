package com.gitsunjaeab.mapick.api.category.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.category.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Getter;


@Getter
public class CategoryResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;


    // 단건 조회용
    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 255)
    private String categoryImage;

    @NotNull
    private OffsetDateTime createdAt;



    public CategoryResponse(String code, String message, LocalDateTime timestamp,
        Long id, String name, String description, String categoryImage,
        OffsetDateTime createdAt) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryImage = categoryImage;
        this.createdAt = createdAt;
    }

    // 데이터를 같이 반환하는 경우 - 단건
    public static CategoryResponse of(Category c) {
        return new CategoryResponse(
            ResponseCode.OK.getCode(),
            "카테고리 조회 성공.",
            LocalDateTime.now(),
            c.getId(),
            c.getName(),
            c.getDescription(),
            c.getCategoryImage(),
            c.getCreatedAt()
        );
    }

    // 데이터 없이 메시지만 반환할 때
//    public static CategoryResponse withoutData(ResponseCode responseCode, String message) {
//        return new CategoryResponse(
//            responseCode.getCode(),
//            message,
//            LocalDateTime.now(),
//            null, null, null, null, null
//        );
//    }
}