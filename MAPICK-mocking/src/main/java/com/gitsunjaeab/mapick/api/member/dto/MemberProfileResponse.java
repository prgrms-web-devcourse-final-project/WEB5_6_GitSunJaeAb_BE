package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Getter
@Setter
public class MemberProfileResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 멤버 프로필 데이터
    private MemberInfo member;

    public MemberProfileResponse(String code, String message, LocalDateTime timestamp, MemberInfo member) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.member = member;
    }

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
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime deletedAt;
    }

    public static MemberProfileResponse of(Member member) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setId(member.getId());
        memberInfo.setName(member.getName());
        memberInfo.setNickname(member.getNickname());
        memberInfo.setEmail(member.getEmail());
        memberInfo.setLoginType(member.getLoginType().name());
        memberInfo.setProvider(member.getProvider());
        memberInfo.setRole(member.getRole());
        memberInfo.setStatus(member.getStatus());
        memberInfo.setProfileImage(member.getProfileImage());
        memberInfo.setCreatedAt(member.getCreatedAt());
        memberInfo.setUpdatedAt(member.getUpdatedAt());
        memberInfo.setDeletedAt(member.getDeletedAt());

        return new MemberProfileResponse(
            ResponseCode.OK.getCode(),
            "회원 프로필 조회 성공",
            LocalDateTime.now(),
            memberInfo
        );
    }
} 