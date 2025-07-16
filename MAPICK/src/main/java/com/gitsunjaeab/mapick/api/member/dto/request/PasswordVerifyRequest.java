package com.gitsunjaeab.mapick.api.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordVerifyRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;
} 