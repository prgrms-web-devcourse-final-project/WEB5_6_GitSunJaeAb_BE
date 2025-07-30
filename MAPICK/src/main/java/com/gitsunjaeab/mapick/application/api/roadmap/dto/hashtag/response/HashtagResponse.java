package com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HashtagResponse {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private OffsetDateTime createdAt;

}