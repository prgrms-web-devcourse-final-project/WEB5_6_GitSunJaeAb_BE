package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
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
//    @NotNull
//    @Size(max = 255)
//    private String loginType;
//
//    @Size(max = 255)
//    private String provider;
//
//
//    @Size(max = 255)
//    private String status;
//
//    @Size(max = 255)
//    private String profileImage;
//
//    private OffsetDateTime lastLogin;
//
//    @NotNull
//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;
}
