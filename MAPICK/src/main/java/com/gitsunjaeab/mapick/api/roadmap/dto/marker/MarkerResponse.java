package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 단일 마커 반환 Response
 */

@Getter
@AllArgsConstructor
public class MarkerResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private MarkerDTO marker;

    public static MarkerResponse of(MarkerDTO dto) {
        return new MarkerResponse(
            ResponseCode.OK.getCode(),
            "마커 조회 성공",
            LocalDateTime.now(),
            dto
        );
    }

}
