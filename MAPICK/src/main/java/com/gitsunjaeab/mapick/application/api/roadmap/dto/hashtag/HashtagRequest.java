package com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HashtagRequest {
    @Size(max = 255)
    private String name;

}