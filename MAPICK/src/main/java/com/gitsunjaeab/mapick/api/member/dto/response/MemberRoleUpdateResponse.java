package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 회원 DTO 반환 Response
 */

@Getter
@AllArgsConstructor
public class MemberRoleUpdateResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;


    public static MemberRoleUpdateResponse set( ) {
        return new MemberRoleUpdateResponse(
            ResponseCode.OK.getCode(),
            "회원 관리자 권한 부여 완료",
            LocalDateTime.now()
        );
    }

    public static MemberRoleUpdateResponse remove( ) {
        return new MemberRoleUpdateResponse(
                ResponseCode.OK.getCode(),
                "회원 관리자 권한 회수 완료",
                LocalDateTime.now()
        );
    }
}
