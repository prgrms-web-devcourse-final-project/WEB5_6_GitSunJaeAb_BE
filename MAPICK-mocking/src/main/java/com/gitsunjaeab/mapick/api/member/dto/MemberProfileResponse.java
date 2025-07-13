package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class MemberProfileResponse {

    private String message;
    private MemberInfo member;

    @Getter
    @Setter
    public static class MemberInfo {
        private Long id;
        private String name;
        private String nickname;
        private String email;
        private String loginType;
        private String provider;
        private String role;
        private String status;
        private String profileImage;
        private String intro;
        private String phone;
        private OffsetDateTime lastLogin;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime deletedAt;
    }

    public static MemberProfileResponse of(Member member) {
        MemberProfileResponse response = new MemberProfileResponse();
        response.setMessage("회원 정보 조회 성공");
        
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setId(member.getId());
        memberInfo.setName(member.getName());
        memberInfo.setNickname(member.getNickname());
        memberInfo.setEmail(member.getEmail());
        memberInfo.setLoginType(member.getLoginType());
        memberInfo.setProvider(member.getProvider());
        memberInfo.setRole(member.getRole());
        memberInfo.setStatus(member.getStatus());
        memberInfo.setProfileImage(member.getProfileImage());
        memberInfo.setIntro(member.getIntro());
        memberInfo.setPhone(member.getPhone());
        memberInfo.setLastLogin(member.getLastLogin());
        memberInfo.setCreatedAt(member.getCreatedAt());
        memberInfo.setUpdatedAt(member.getUpdatedAt());
        memberInfo.setDeletedAt(member.getDeletedAt());
        
        response.setMember(memberInfo);
        return response;
    }
} 