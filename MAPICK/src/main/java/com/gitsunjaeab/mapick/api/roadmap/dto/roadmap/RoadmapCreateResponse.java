package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RoadmapCreateResponse implements BaseApiResponse {

    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    public static RoadmapCreateResponse of(ResponseCode responseCode, String message) {
        return new RoadmapCreateResponse(
                responseCode.getCode(),
                message,
                LocalDateTime.now()
        );
    }
}