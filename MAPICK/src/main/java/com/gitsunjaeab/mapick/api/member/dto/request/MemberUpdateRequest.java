package com.gitsunjaeab.mapick.api.member.dto.request;

import com.gitsunjaeab.mapick.api.member.dto.MemberEmailUnique;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {

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
} 