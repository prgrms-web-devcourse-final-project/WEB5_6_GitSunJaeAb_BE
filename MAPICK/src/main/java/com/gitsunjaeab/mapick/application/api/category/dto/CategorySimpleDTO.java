package com.gitsunjaeab.mapick.application.api.category.dto;

import com.gitsunjaeab.mapick.application.domain.category.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySimpleDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    public CategorySimpleDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
