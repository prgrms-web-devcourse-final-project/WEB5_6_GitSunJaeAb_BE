package com.gitsunjaeab.mapick.api.member.dto.response;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class MemberProfileUpdateResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 업데이트된 멤버 정보
    private MemberInfo member;

    public MemberProfileUpdateResponse(String code, String message, LocalDateTime timestamp, MemberInfo member) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.member = member;
    }

    @Getter
    @Setter
    public static class MemberInfo {
        private Long id;
        private String nickname;
        private String profileImage;
    }

    public static MemberProfileUpdateResponse of(Member member) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setId(member.getId());
        memberInfo.setNickname(member.getNickname());
        memberInfo.setProfileImage(member.getProfileImage());

        return new MemberProfileUpdateResponse(
            ResponseCode.OK.getCode(),
            "회원 정보가 성공적으로 수정되었습니다.",
            LocalDateTime.now(),
            memberInfo
        );
    }
} 