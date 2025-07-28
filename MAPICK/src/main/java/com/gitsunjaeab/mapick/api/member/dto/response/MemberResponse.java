package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

/**
 * 회원 DTO 반환 Response
 */

@Getter
@AllArgsConstructor
public class MemberResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private MemberDTO memberDTO;

    public static MemberResponse of(MemberDTO memberDTO) {
        return new MemberResponse(
            ResponseCode.OK.getCode(),
            "회원 조회 성공",
            OffsetDateTime.now(),
            memberDTO
        );
    }
}
