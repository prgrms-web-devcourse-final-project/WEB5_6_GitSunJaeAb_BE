package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.response;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapAchievementDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class RoadmapCreateResponse implements BaseApiResponse {

    private final String code;
    private final String message;
    private final OffsetDateTime timestamp;
    private final Long roadmapId;

    public static RoadmapCreateResponse createShared(Long roadmapId) {
        return new RoadmapCreateResponse(
                ResponseCode.OK.getCode(),
                "공유지도 생성 완료",
                OffsetDateTime.now(),
                roadmapId
        );
    }

    public static RoadmapCreateResponse createPersonal(Long roadmapId) {
        return new RoadmapCreateResponse(
            ResponseCode.OK.getCode(),
            "로드맵 생성 완료",
            OffsetDateTime.now(),
            roadmapId
        );
    }

    public static RoadmapCreateResponse createWithAchievement(
        RoadmapAchievementDTO roadmapAchievementDTO) {
        return new RoadmapCreateResponse(
            ResponseCode.OK.getCode(),
            "공유지도 생성 완료! 업적 '" + roadmapAchievementDTO.getAchievement().getName() + "' 을(를) 획득했습니다.",
            OffsetDateTime.now(),
            roadmapAchievementDTO.getRoadmapId()
        );
    }
}