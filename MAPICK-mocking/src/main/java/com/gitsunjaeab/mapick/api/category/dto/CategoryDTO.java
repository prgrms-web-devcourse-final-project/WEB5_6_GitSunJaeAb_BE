package com.gitsunjaeab.mapick.api.category.dto;

import com.gitsunjaeab.mapick.domain.category.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 255)
    private String categoryImage;

    @NotNull
    private OffsetDateTime createdAt;
}
