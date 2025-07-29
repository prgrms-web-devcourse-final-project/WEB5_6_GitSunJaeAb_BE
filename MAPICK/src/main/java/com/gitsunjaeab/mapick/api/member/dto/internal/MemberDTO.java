package com.gitsunjaeab.mapick.api.member.dto.internal;

import com.gitsunjaeab.mapick.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    @NotNull
    private boolean isBlacklisted;

    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String nickname;

    @Size(max = 255)
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

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    public static MemberDTO of(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .isBlacklisted(member.getIsBlacklisted())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .loginType(member.getLoginType().toString())
                .provider(member.getProvider())
                .role(member.getRole())
                .status(member.getStatus())
                .profileImage(member.getProfileImage())
                .lastLogin(member.getLastLogin())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .deletedAt(member.getDeletedAt())
                .build();
    }
}
