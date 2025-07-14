package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerListDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private Integer layerSeq;

    private LocalDate layerTime;

    @NotNull
    private OffsetDateTime createdAt;

    private MemberSimpleDTO member;

    private Long roadmap;
}
