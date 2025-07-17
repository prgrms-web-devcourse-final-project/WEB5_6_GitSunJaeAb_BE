package com.gitsunjaeab.mapick.api.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 비밀번호 검증 요청 DTO
 */
@Getter
@Setter
public class PasswordVerifyRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;
} 