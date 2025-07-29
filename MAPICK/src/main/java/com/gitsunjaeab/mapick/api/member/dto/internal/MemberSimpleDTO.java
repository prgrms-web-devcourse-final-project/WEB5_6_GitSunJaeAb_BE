package com.gitsunjaeab.mapick.api.member.dto.internal;

import com.gitsunjaeab.mapick.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSimpleDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String nickname;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String profileImage;

    @Size(max = 255)
    private String role;



    public MemberSimpleDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.profileImage = member.getProfileImage() != null ? member.getProfileImage() : null;
        this.role = member.getRole();
    }

    public static MemberSimpleDTO from(Member member) {
        if (member == null) return null;
        return new MemberSimpleDTO(member);
    }

    public static MemberSimpleDTO of(Member member ) {
        return MemberSimpleDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .role(member.getRole())
                .build();
    }


}


