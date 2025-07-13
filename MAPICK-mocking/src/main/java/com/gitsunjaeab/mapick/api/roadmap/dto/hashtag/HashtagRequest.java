package com.gitsunjaeab.mapick.api.roadmap.dto.hashtag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HashtagRequest {

    @NotNull
    @Size(max = 255)
    private String name;

}