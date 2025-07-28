package com.gitsunjaeab.mapick.api.achievement.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberAchievementListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<MemberAchievementDTO> memberAchievements;

    public static MemberAchievementListResponse getList(List<MemberAchievementDTO> memberAchievements) {
        return new MemberAchievementListResponse(
            ResponseCode.OK.getCode(),
            "사용자 업적 목록 조회 성공",
            OffsetDateTime.now(),
            memberAchievements
        );
    }
}
