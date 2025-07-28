package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 회원 DTO 반환 Response
 */

@Getter
@AllArgsConstructor
public class MemberBlackListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;


    public static MemberBlackListResponse set( ) {
        return new MemberBlackListResponse(
            ResponseCode.OK.getCode(),
            "회원 블랙 리스트 설정 완료",
                OffsetDateTime.now()
        );
    }

    public static MemberBlackListResponse remove( ) {
        return new MemberBlackListResponse(
                ResponseCode.OK.getCode(),
                "회원 블랙 리스트 해제 완료",
                OffsetDateTime.now()
        );
    }
}
