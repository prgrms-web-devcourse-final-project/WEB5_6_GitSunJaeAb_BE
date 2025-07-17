package com.gitsunjaeab.mapick.api.member.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 프로필 정보 수정 요청 DTO
 */
@Getter
@Setter
public class MemberProfileUpdateRequest {

    @Size(max = 255, message = "닉네임은 255자 이하여야 합니다.")
    private String nickname;

    @Size(max = 500, message = "프로필 이미지 URL은 500자 이하여야 합니다.")
    private String profileImage;
} 