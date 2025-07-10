package com.gitsunjaeab.mapick.roadmap_category_relation.dto;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoadmapCategoryRelationResponseDTO {

    private Long id;
    private OffsetDateTime createdAt;
    private Long map;

}
