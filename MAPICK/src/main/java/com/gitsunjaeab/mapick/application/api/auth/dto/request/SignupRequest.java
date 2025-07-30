package com.gitsunjaeab.mapick.application.api.auth.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank
    @Schema(description = "닉네임 (중복 불가)", example = "mapick_user")
    private String nickname;

    @NotBlank
    @Email
    @Schema(description = "이메일 (중복 불가)", example = "user@example.com")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$",
            message = "비밀번호는 8자 이상, 12자 이하 영문+숫자를 포함해야 합니다."
    )
    @Schema(description = "비밀번호 (8자 이상, 12자 이하 영문+숫자 포함)", example = "password123")
    private String password;

}