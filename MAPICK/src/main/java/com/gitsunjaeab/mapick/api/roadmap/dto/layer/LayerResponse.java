package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private LayerDetailDTO layer;
    private boolean isZzim;


    public static LayerResponse of(Layer layer, boolean isZzim) {
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            "레이어 조회 성공",
            LocalDateTime.now(),
            LayerDetailDTO.from(layer, isZzim),
            isZzim
        );
    }
}

