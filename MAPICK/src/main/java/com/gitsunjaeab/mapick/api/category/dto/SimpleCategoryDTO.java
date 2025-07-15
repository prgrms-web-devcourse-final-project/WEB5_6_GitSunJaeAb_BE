package com.gitsunjaeab.mapick.api.category.dto;

import com.gitsunjaeab.mapick.domain.category.Category;
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
public class SimpleCategoryDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    public SimpleCategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
