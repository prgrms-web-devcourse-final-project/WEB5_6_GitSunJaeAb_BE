package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
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

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.isBlacklisted = member.getIsBlacklisted();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.loginType = member.getLoginType().name();
        this.provider = member.getProvider();
        this.role = member.getRole();
        this.status = member.getStatus();
        this.profileImage = member.getProfileImage();
        this.lastLogin = member.getLastLogin();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
        this.deletedAt = member.getDeletedAt();
    }
}
