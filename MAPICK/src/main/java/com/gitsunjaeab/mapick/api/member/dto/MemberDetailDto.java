package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    private Long id;

    @NotNull
    private boolean isBlacklisted;

    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String nickname;

    @Size(max = 255)
    @MemberEmailUnique
    private String email;

    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String loginType;

    @Size(max = 255)
    private String provider;

    @NotNull
    @Size(max = 255)
    private String role;

    @Size(max = 255)
    private String status;

    @Size(max = 255)
    private String profileImage;

    private OffsetDateTime lastLogin;

    @Size(max = 255)
    private List<MemberInterestDTO> memberInterests;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;
}
