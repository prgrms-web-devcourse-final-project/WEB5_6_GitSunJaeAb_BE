package com.gitsunjaeab.mapick.map_category_relation;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapCategoryRelationDTO {

    private Long id;
    private OffsetDateTime createdAt;
    private Long map;

}
