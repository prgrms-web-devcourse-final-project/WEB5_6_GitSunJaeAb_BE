package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
public class MemberResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 멤버 데이터 필드들
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

    public MemberResponse(String code, String message, LocalDateTime timestamp,
                         Long id, boolean isBlacklisted, String name, String nickname,
                         String email, String password, String loginType, String provider,
                         String role, String status, String profileImage, OffsetDateTime lastLogin,
                         OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.id = id;
        this.isBlacklisted = isBlacklisted;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.provider = provider;
        this.role = role;
        this.status = status;
        this.profileImage = profileImage;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(
            ResponseCode.OK.getCode(),
            "회원 조회 성공",
            LocalDateTime.now(),
            member.getId(),
            member.getIsBlacklisted(),
            member.getName(),
            member.getNickname(),
            member.getEmail(),
            member.getPassword(),
            member.getLoginType().name(),
            member.getProvider(),
            member.getRole(),
            member.getStatus(),
            member.getProfileImage(),
            member.getLastLogin(),
            member.getCreatedAt(),
            member.getUpdatedAt(),
            member.getDeletedAt()
        );
    }
}
