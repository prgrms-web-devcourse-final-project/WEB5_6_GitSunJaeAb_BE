package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarkerCustomImageResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MarkerCustomImageDTO> markerCustomImages;

    public static MarkerCustomImageResponse create() {
        return new MarkerCustomImageResponse(
            ResponseCode.OK.getCode(),
            "커스텀 마커 이미지 생성 완료",
            OffsetDateTime.now(),
            null
        );
    }

    public static MarkerCustomImageResponse getList(List<MarkerCustomImageDTO> markerCustomImages) {
        return new MarkerCustomImageResponse(
            ResponseCode.OK.getCode(),
            "커스텀 마커 이미지 목록 조회 성공",
            OffsetDateTime.now(),
            markerCustomImages
        );
    }

    public static MarkerCustomImageResponse update() {
        return new MarkerCustomImageResponse(
            ResponseCode.OK.getCode(),
            "커스텀 마커 이미지 수정 완료",
            OffsetDateTime.now(),
            null
        );
    }

    public static MarkerCustomImageResponse delete() {
        return new MarkerCustomImageResponse(
            ResponseCode.OK.getCode(),
            "커스텀 마커 이미지 삭제 완료",
            OffsetDateTime.now(),
            null
        );
    }

}
