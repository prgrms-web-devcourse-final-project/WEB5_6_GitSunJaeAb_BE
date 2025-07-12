package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
public class MemberResponse {

    private Long id;
    private boolean isBlacklisted;
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String loginType;
    private String provider;
    private String role;
    private String status;
    private String profileImage;
    private OffsetDateTime lastLogin;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.isBlacklisted = member.getIsBlacklisted();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.loginType = member.getLoginType();
        this.provider = member.getProvider();
        this.role = member.getRole();
        this.status = member.getStatus();
        this.profileImage = member.getProfileImage();
        this.lastLogin = member.getLastLogin();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
        this.deletedAt = member.getDeletedAt();
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member);
    }
}
