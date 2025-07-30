package com.gitsunjaeab.mapick.application.api.member.dto.response;

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
public class MemberInterestResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;

    public static MemberInterestResponse update( ) {
        return new MemberInterestResponse(
            ResponseCode.OK.getCode(),
            "회원 관심분야 추가 성공",
                OffsetDateTime.now()
        );
    }

    public static MemberInterestResponse delete( ) {
        return new MemberInterestResponse(
                ResponseCode.OK.getCode(),
                "회원 관심분야 수정 성공",
                OffsetDateTime.now()
        );
    }



}
