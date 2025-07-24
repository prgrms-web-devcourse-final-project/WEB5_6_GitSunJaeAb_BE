package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SharedRoadmapUpdateResponse implements BaseApiResponse {
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    public static SharedRoadmapUpdateResponse of(ResponseCode responseCode, String message) {
        return new SharedRoadmapUpdateResponse(
                responseCode.getCode(),
                message,
                LocalDateTime.now()
        );
    }

}
