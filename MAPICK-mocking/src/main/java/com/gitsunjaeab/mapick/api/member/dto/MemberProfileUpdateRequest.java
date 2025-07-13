package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberProfileUpdateRequest {

    @Size(max = 255, message = "닉네임은 255자 이하여야 합니다.")
    private String nickname;

    @Size(max = 500, message = "프로필 이미지 URL은 500자 이하여야 합니다.")
    private String profileImage;

    @Size(max = 1000, message = "소개는 1000자 이하여야 합니다.")
    private String intro;

    @Size(max = 20, message = "전화번호는 20자 이하여야 합니다.")
    private String phone;
} 