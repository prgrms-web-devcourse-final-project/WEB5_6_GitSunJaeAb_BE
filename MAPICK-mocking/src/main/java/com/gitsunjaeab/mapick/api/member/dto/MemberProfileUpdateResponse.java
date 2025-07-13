package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.member.Member;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberProfileUpdateResponse {

    private String message;
    private MemberInfo member;

    @Getter
    @Setter
    public static class MemberInfo {
        private Long id;
        private String nickname;
        private String profileImage;
    }

    public static MemberProfileUpdateResponse of(Member member) {
        MemberProfileUpdateResponse response = new MemberProfileUpdateResponse();
        response.setMessage("회원 정보가 성공적으로 수정되었습니다.");
        
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setId(member.getId());
        memberInfo.setNickname(member.getNickname());
        memberInfo.setProfileImage(member.getProfileImage());
        
        response.setMember(memberInfo);
        return response;
    }
} 