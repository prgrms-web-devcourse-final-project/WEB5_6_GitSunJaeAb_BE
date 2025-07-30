package com.gitsunjaeab.mapick.application.api.category.dto.internal;

import com.gitsunjaeab.mapick.application.domain.category.Category;
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

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.categoryImage = category.getCategoryImage();
        this.createdAt = category.getCreatedAt();
    }
}
