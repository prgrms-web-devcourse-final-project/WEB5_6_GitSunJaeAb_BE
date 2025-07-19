package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class MemberInterestDTO {

    private Long id;

    @NotNull
    private OffsetDateTime createdAt;

    private List<CategorySimpleDTO> categories;

}
