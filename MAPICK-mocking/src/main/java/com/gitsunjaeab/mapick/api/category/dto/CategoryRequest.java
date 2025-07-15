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
}