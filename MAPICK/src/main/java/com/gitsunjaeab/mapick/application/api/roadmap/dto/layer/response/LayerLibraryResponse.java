package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.LayerDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerLibraryResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    // 레이어 라이브러리 필드
    private Long id;
    private OffsetDateTime createdAt;
    private OffsetDateTime deletedAt;

    private LayerDTO layer;

    public static LayerLibraryResponse of(LayerLibrary layerLibrary) {
        return new LayerLibraryResponse(
            ResponseCode.OK.getCode(),
            "레이어 라이브러리 조회 성공",
            OffsetDateTime.now(),
            layerLibrary.getId(),
            layerLibrary.getCreatedAt(),
            layerLibrary.getDeletedAt(),
            new LayerDTO(layerLibrary.getLayer())
        );
    }
}