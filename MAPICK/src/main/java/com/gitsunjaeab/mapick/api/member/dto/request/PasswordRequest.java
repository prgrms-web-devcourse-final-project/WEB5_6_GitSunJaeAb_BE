package com.gitsunjaeab.mapick.api.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 비밀번호 검증/수정 요청 DTO
 */

@Getter
@Setter
public class PasswordRequest {

    @NotBlank
    private String password;
}
