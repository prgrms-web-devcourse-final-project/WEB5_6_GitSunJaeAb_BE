package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
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
    private String email;

    @NotNull
    @Size(max = 255)
    private String role;

    public static MemberListDTO of(Member member ) {
        return MemberListDTO.builder()
                .id(member.getId())
                .isBlacklisted(member.getIsBlacklisted())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
