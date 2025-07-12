package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 목록(List) 반환 Response
 */
@Getter
@AllArgsConstructor
public class MemberListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<MemberListDTO> members;

    public static MemberListResponse of(List<Member> memberEntities){
        List<MemberListDTO> memberDtos = memberEntities.stream()
            .map(m -> new MemberListDTO(
                m.getId(),
                m.getIsBlacklisted(),
                m.getRole(),
                m.getName(),
                m.getNickname(),
                m.getEmail()
            )).toList();

        return new MemberListResponse(
            ResponseCode.OK.getCode(),
            "전체 회원 조회 성공",
            LocalDateTime.now(),
            memberDtos
        );
    }
}
