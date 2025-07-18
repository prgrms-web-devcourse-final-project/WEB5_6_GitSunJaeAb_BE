package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 회원 DTO 반환 Response
 */

@Getter
@AllArgsConstructor
public class MemberResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private MemberDTO memberDTO;

    public static MemberResponse of(MemberDTO memberDTO) {
        return new MemberResponse(
            ResponseCode.OK.getCode(),
            "회원 조회 성공",
            LocalDateTime.now(),
            memberDTO
        );
    }
}
