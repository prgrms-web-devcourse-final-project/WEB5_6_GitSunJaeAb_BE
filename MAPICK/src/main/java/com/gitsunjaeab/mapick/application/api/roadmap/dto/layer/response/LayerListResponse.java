package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerListDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<LayerListDTO> layers;

    public static LayerListResponse getList(List<LayerListDTO> layers) {
        return new LayerListResponse(
            ResponseCode.OK.getCode(),
            "레이어 목록 조회 성공",
            OffsetDateTime.now(),
            layers
        );
    }
}

