package com.gitsunjaeab.mapick.api.achievement.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AchievementListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<AchievementDTO> achievements;

    public static AchievementListResponse getList(List<AchievementDTO> achievements) {
        return new AchievementListResponse(
            ResponseCode.OK.getCode(),
            "전체 업적 목록 조회 성공",
            OffsetDateTime.now(),
            achievements
        );
    }
}
