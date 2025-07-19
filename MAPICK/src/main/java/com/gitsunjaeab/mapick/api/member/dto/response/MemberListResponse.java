package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberListDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원 목록(List) DTO 반환 Response
 */

@Getter
@AllArgsConstructor
public class MemberListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<MemberListDTO> members;

    public static MemberListResponse of(List<MemberListDTO> memberListDTOs){

        return new MemberListResponse(
            ResponseCode.OK.getCode(),
            "전체 회원 조회 성공",
            LocalDateTime.now(),
            memberListDTOs
        );
    }
}
