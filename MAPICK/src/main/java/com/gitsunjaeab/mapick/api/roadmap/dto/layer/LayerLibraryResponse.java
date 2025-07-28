package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
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