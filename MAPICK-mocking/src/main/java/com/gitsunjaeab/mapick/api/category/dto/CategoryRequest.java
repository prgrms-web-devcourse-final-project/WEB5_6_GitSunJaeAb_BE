package com.gitsunjaeab.mapick.api.category.dto;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class CategoryRequest {

    @Size(max = 255)
    private String name;

    private String description;

    private MultipartFile imageFile; // MultipartFile 받는 필드

    @Size(max = 255)
    private String categoryImage; // 업로드된 파일의 URL

    private OffsetDateTime createdAt = OffsetDateTime.now(); // 기본값 자동처리


}