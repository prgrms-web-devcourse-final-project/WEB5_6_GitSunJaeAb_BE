package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.OffsetDateTime;

import com.gitsunjaeab.mapick.domain.roadmap.Marker;
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
    private OffsetDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MarkerDTO marker;

    public static MarkerResponse create() {
        return new MarkerResponse(
            ResponseCode.OK.getCode(),
            "마커 생성 완료",
            OffsetDateTime.now(),
            null
        );
    }

    public static MarkerResponse get(MarkerDTO dto) {
        return new MarkerResponse(
            ResponseCode.OK.getCode(),
            "마커 조회 성공",
            OffsetDateTime.now(),
            dto
        );
    }

    public static MarkerResponse update() {
        return new MarkerResponse(
            ResponseCode.OK.getCode(),
            "마커 수정 완료",
            OffsetDateTime.now(),
            null
        );
    }

    public static MarkerResponse delete() {
        return new MarkerResponse(
            ResponseCode.OK.getCode(),
            "마커 삭제 완료",
            OffsetDateTime.now(),
            null
        );
    }

    public static MarkerResponse of(Marker marker, String message) {
        return new MarkerResponse(
                ResponseCode.OK.getCode(),
                message,
                OffsetDateTime.now(),
                new MarkerDTO(marker)
        );
    }
}
