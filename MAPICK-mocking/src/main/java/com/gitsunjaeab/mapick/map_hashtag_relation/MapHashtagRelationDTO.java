package com.gitsunjaeab.mapick.map_hashtag_relation;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapHashtagRelationDTO {

    private Long id;
    private OffsetDateTime createdAt;
    private Long hashtag;
    private Long map;

}
