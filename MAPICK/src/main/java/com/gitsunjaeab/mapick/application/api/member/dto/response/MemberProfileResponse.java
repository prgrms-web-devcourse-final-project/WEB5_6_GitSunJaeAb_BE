package com.gitsunjaeab.mapick.application.api.member.dto.response;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberDetailDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

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
    private OffsetDateTime timestamp;
    private MemberDetailDTO memberDetailDto;

    public static MemberProfileResponse of(MemberDetailDTO memberDetailDto) {

        return new MemberProfileResponse(
            ResponseCode.OK.getCode(),
            "회원 프로필 조회 성공",
            OffsetDateTime.now(),
            memberDetailDto
        );
    }
}