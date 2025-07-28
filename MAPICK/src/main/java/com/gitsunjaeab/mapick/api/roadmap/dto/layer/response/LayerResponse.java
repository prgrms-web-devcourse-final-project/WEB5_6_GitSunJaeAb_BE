package com.gitsunjaeab.mapick.api.roadmap.dto.layer.response;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerDetailDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private LayerDetailDTO layer;


    public static LayerResponse of(Layer layer, boolean isZzim, String message) {
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            LayerDetailDTO.from(layer, isZzim)
        );
    }

    public static LayerResponse of(LayerDetailDTO layerDetailDTO, String message) {
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            layerDetailDTO
        );
    }
}

