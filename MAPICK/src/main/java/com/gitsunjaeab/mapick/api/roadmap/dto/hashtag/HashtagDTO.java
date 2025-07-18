package com.gitsunjaeab.mapick.api.roadmap.dto.hashtag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class HashtagDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    public HashtagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
