package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberInterestResponse {

    private Long id;

    @NotNull
    private OffsetDateTime createdAt;

    private Long interest;

    private Long member;

}
