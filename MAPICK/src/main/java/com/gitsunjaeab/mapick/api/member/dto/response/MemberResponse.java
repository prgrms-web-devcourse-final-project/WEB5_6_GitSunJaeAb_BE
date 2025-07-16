package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Getter
@AllArgsConstructor
public class MemberResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private MemberInfo member;


    @Getter
    @Setter
    public static class MemberInfo {
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
    }

    public static MemberResponse of(Member member) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setId(member.getId());
        memberInfo.setBlacklisted(member.getIsBlacklisted());
        memberInfo.setName(member.getName());
        memberInfo.setNickname(member.getNickname());
        memberInfo.setEmail(member.getEmail());
        memberInfo.setPassword(member.getPassword());
        memberInfo.setLoginType(member.getLoginType().name());
        memberInfo.setProvider(member.getProvider());
        memberInfo.setRole(member.getRole());
        memberInfo.setStatus(member.getStatus());
        memberInfo.setProfileImage(member.getProfileImage());
        memberInfo.setLastLogin(member.getLastLogin());
        memberInfo.setCreatedAt(member.getCreatedAt());
        memberInfo.setUpdatedAt(member.getUpdatedAt());
        memberInfo.setDeletedAt(member.getDeletedAt());

        return new MemberResponse(
            ResponseCode.OK.getCode(),
            "회원 조회 성공",
            LocalDateTime.now(),
            memberInfo
        );
    }
}
