package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberDetailDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 회원 + 회원의 관심분야 DTO 반환 Response
 */

@Getter
@Setter
@AllArgsConstructor
public class MemberProfileResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private MemberDetailDTO memberDetailDto;

    public static MemberProfileResponse of(MemberDetailDTO memberDetailDto) {

        return new MemberProfileResponse(
            ResponseCode.OK.getCode(),
            "회원 프로필 조회 성공",
            LocalDateTime.now(),
            memberDetailDto
        );
    }
}