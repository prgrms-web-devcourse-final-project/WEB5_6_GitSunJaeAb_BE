package com.gitsunjaeab.mapick.roadmap_hashtag_relation.dto;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoadmapHashtagRelationDTO {

    private Long id;
    private OffsetDateTime createdAt;
    private Long hashtag;
    private Long map;

}
