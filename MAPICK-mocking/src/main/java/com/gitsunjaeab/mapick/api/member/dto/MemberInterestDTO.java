package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberInterestDTO {

    private Long id;

    @NotNull
    private OffsetDateTime createdAt;

    private List<Long> category;

    private Long member;

}
