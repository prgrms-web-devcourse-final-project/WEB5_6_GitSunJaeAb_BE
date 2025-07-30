package com.gitsunjaeab.mapick.application.api.category.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRequest {

    @Size(max = 255)
    private String name;

    private String description;
}