package com.gitsunjaeab.mapick.hashtag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HashtagRequestDTO {

//    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

//    private OffsetDateTime createdAt;

}