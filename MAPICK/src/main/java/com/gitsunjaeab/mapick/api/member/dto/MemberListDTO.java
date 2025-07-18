package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberListDTO {

    private Long id;

    private boolean isBlacklisted;

    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String nickname;

    @Size(max = 255)
    @MemberEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String role;
}
