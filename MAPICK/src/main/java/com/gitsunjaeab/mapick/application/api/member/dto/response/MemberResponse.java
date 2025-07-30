package com.gitsunjaeab.mapick.application.api.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MemberDTO memberDTO;

    public static MemberResponse of(MemberDTO memberDTO) {
        return new MemberResponse(
            ResponseCode.OK.getCode(),
            "회원 조회 성공",
            OffsetDateTime.now(),
            memberDTO
        );
    }

    public static MemberResponse delete( ) {
        return new MemberResponse(
                ResponseCode.OK.getCode(),
                "회원 삭제 성공",
                OffsetDateTime.now(),
                null
        );
    }

    public static MemberResponse update( ) {
        return new MemberResponse(
                ResponseCode.OK.getCode(),
                "회원 정보 수정 성공",
                OffsetDateTime.now(),
                null
        );
    }

    public static MemberResponse withdraw( ) {
        return new MemberResponse(
                ResponseCode.OK.getCode(),
                "회원 탈퇴 성공",
                OffsetDateTime.now(),
                null
        );
    }

    // 비밀번호 검증 성공
    public static MemberResponse verifyPassword( ) {
        return new MemberResponse(
                ResponseCode.VERITY_PASSWORD_SUCCESS.getCode(),
                ResponseCode.VERITY_PASSWORD_SUCCESS.getMessage(),
                OffsetDateTime.now(),
                null
        );
    }

}
