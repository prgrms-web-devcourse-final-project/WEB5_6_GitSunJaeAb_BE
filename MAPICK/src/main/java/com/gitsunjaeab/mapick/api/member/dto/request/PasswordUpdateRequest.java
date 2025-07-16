package com.gitsunjaeab.mapick.api.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 비밀번호 수정 요청 DTO
 */
@Getter
@Setter
public class PasswordUpdateRequest {

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하여야 합니다.")
    private String newPassword;
} 