package com.gitsunjaeab.mapick.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapResponseDTO {

//    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String description;

    @Size(max = 255)
    private String thumbnail;

    @NotNull
    @JsonProperty("isPublic")
    private Boolean isPublic;

    @NotNull
    @JsonProperty("isAnimated")
    private Boolean isAnimated;

    private Integer likeCount;

    private Integer viewCount;

//    @NotNull
//    @Size(max = 255)
//    private String mapType;
//
//    @NotNull
//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;
//
//    private Long member;
//
//    private Long originalMap;

}
