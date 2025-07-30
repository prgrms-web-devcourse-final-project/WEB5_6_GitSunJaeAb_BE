package com.gitsunjaeab.mapick.application.api.member.dto.internal;

import com.gitsunjaeab.mapick.application.domain.member.Member;
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
public class MemberDetailDTO {

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

    private Long loginCount;

    @Size(max = 255)
    private List<MemberInterestDTO> memberInterests; // 회원의 관심 분야

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    public static MemberDetailDTO of(Member member,List<MemberInterestDTO> memberInterestDTOList ) {
        return MemberDetailDTO.builder()
                .id(member.getId())
                .isBlacklisted(member.getIsBlacklisted())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .loginType(member.getLoginType().name())
                .provider(member.getProvider())
                .role(member.getRole())
                .status(member.getStatus())
                .profileImage(member.getProfileImage())
                .lastLogin(member.getLastLogin())
                .memberInterests(memberInterestDTOList)
                .loginCount(member.getLoginCount())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .deletedAt(member.getDeletedAt())
                .build();
    }

}
