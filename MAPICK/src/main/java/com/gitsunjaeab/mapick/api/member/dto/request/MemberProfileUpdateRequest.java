package com.gitsunjaeab.mapick.api.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 프로필 정보 수정 요청 DTO
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 프로필 정보 수정 요청 DTO")
public class MemberProfileUpdateRequest {

    @Schema(description = "변경할 닉네임", example = "나단킴")
    @Size(max = 255, message = "닉네임은 255자 이하여야 합니다.")
    private String nickname;

//    @Size(max = 500, message = "프로필 이미지 URL은 500자 이하여야 합니다.")
//    private String profileImage;
} 