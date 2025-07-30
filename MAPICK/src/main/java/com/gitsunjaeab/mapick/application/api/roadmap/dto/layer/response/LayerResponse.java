package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.LayerDetailDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LayerResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private LayerDetailDTO layer;

    // 포크 정보 필드 (간단하게)
    private Long forkedFromLayerId; // 포크한 원본 레이어 ID
    private String forkedFromRoadmapTitle; // 포크한 원본 로드맵 제목

    // 생성자 추가
    public LayerResponse() {}

    public LayerResponse(String code, String message, OffsetDateTime timestamp, LayerDetailDTO layer, 
                        Long forkedFromLayerId, String forkedFromRoadmapTitle) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.layer = layer;
        this.forkedFromLayerId = forkedFromLayerId;
        this.forkedFromRoadmapTitle = forkedFromRoadmapTitle;
    }


    public static LayerResponse create(Layer layer, boolean isZzim, String message) {
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            LayerDetailDTO.from(layer, isZzim),
            null, null
        );
    }

    public static LayerResponse get(LayerDetailDTO layerDetailDTO, String message) {
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            layerDetailDTO,
            null, null
        );
    }

    public static LayerResponse update(Layer layer, boolean isZzim, String message) {
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            LayerDetailDTO.from(layer, isZzim),
            null, null
        );
    }

    public static LayerResponse delete(Layer layer, boolean isZzim, String message) {
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            LayerDetailDTO.from(layer, isZzim),
            null, null
        );
    }

    public static LayerResponse createFork(Layer forkedLayer, Layer originLayer, Roadmap originRoadmap, Roadmap targetRoadmap, boolean isForked, String message) {
        // 포크된 레이어의 roadmap 정보는 타겟 로드맵으로 설정
        LayerDetailDTO forkedLayerDTO = LayerDetailDTO.from(forkedLayer, false);
        forkedLayerDTO = new LayerDetailDTO(
            forkedLayerDTO.getId(),
            forkedLayerDTO.getName(),
            forkedLayerDTO.getDescription(),
            forkedLayerDTO.getLayerSeq(),
            forkedLayerDTO.isZzim(),
            forkedLayerDTO.getCreatedAt(),
            forkedLayerDTO.getUpdatedAt(),
            forkedLayerDTO.getDeletedAt(),
            forkedLayerDTO.getMember(),
            targetRoadmap != null ? new RoadmapSimpleDTO(targetRoadmap) : null // 타겟 로드맵 정보 포함
        );
        
        return new LayerResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            forkedLayerDTO,
            originLayer != null ? originLayer.getId() : null,
            originRoadmap != null ? originRoadmap.getTitle() : null
        );
    }
}

