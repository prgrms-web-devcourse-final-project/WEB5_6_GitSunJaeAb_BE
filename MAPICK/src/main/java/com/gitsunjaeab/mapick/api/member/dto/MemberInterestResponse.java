package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 회원 관심분야 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInterestResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 관심분야 데이터 필드들
    private Long id;
    private OffsetDateTime createdAt;
    private Long categoryId;
    private String categoryName;
    private Long memberId;
    private String memberNickname;

    /**
     * MemberInterest 엔티티를 Response로 변환 (단순 데이터만)
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
    
    /**
     * 관심분야 생성 응답 (커스텀 응답 + 데이터)
     */
    public static MemberInterestResponse ofCreate(MemberInterest memberInterest) {
        return new MemberInterestResponse(
            ResponseCode.OK.getCode(),
            "관심분야 선택 완료",
            LocalDateTime.now(),
            memberInterest.getId(),
            memberInterest.getCreatedAt(),
            memberInterest.getCategory() != null ? memberInterest.getCategory().getId() : null,
            memberInterest.getCategory() != null ? memberInterest.getCategory().getName() : null,
            memberInterest.getMember() != null ? memberInterest.getMember().getId() : null,
            memberInterest.getMember() != null ? memberInterest.getMember().getNickname() : null
        );
    }
}
