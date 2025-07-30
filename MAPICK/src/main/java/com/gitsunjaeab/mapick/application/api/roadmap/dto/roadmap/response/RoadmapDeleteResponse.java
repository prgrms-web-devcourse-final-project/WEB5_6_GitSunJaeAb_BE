package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
@Getter
@AllArgsConstructor
public class RoadmapDeleteResponse implements BaseApiResponse {
    private final String code;
    private final String message;
    private final OffsetDateTime timestamp;

    public static RoadmapDeleteResponse deleteRoadmap(ResponseCode responseCode, String message) {
        return new RoadmapDeleteResponse(
                responseCode.getCode(),
                message,
                OffsetDateTime.now()
        );
    }

}
