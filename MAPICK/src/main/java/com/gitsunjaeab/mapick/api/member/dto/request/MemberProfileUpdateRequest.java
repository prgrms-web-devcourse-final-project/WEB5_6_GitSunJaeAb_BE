package com.gitsunjaeab.mapick.api.member.dto.request;

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
} 