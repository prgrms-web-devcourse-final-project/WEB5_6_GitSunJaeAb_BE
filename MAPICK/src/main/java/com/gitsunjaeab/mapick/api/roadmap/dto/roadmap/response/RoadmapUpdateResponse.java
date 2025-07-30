package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class RoadmapUpdateResponse implements BaseApiResponse {
    private final String code;
    private final String message;
    private final OffsetDateTime timestamp;

    public static RoadmapUpdateResponse updateRoadmap(ResponseCode responseCode, String message) {
        return new RoadmapUpdateResponse(
                responseCode.getCode(),
                message,
                OffsetDateTime.now()
        );
    }

    public static RoadmapUpdateResponse updateSharedRoadmap(ResponseCode responseCode, String message) {
        return new RoadmapUpdateResponse(
                responseCode.getCode(),
                message,
                OffsetDateTime.now()
        );
    }

}
