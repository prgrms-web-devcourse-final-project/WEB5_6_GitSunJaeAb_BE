package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * 회원 관심분야 응답 DTO
 */
@Getter
@Setter
public class MemberInterestResponse {

    private Long id;
    private OffsetDateTime createdAt;
    private Long categoryId;
    private String categoryName;
    private Long memberId;
    private String memberNickname;

    /**
     * MemberInterest 엔티티를 Response로 변환
     */
    public static MemberInterestResponse of(MemberInterest memberInterest) {
        MemberInterestResponse response = new MemberInterestResponse();
        response.setId(memberInterest.getId());
        response.setCreatedAt(memberInterest.getCreatedAt());
        
        if (memberInterest.getCategory() != null) {
            response.setCategoryId(memberInterest.getCategory().getId());
            response.setCategoryName(memberInterest.getCategory().getName());
        }
        
        if (memberInterest.getMember() != null) {
            response.setMemberId(memberInterest.getMember().getId());
            response.setMemberNickname(memberInterest.getMember().getNickname());
        }
        
        return response;
    }
}
