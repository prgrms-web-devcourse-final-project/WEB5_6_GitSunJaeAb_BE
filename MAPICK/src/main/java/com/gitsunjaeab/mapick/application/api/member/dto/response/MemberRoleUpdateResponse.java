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
public class MemberRoleUpdateResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;


    public static MemberRoleUpdateResponse set( ) {
        return new MemberRoleUpdateResponse(
            ResponseCode.OK.getCode(),
            "회원 관리자 권한 부여 완료",
                OffsetDateTime.now()
        );
    }

    public static MemberRoleUpdateResponse remove( ) {
        return new MemberRoleUpdateResponse(
                ResponseCode.OK.getCode(),
                "회원 관리자 권한 회수 완료",
                OffsetDateTime.now()
        );
    }
}
