package com.gitsunjaeab.mapick.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryRequestDTO {

//    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 255)
    private String categoryImage;

//    @NotNull
//    private OffsetDateTime createdAt;
//
//    private Long mapCategoryRelations;

}