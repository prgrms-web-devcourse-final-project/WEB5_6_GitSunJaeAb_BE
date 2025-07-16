package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
public class LayerResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private LayerInfo Layer;

    public LayerResponse(String code, String message, LocalDateTime timestamp, LayerInfo layer) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.Layer = layer;
    }


    @Getter
    @Setter
    public static class LayerInfo {
        private Long id;

        @NotNull
        @Size(max = 255)
        private String name;

        private String description;
        private Integer layerSeq;
        private LocalDate layerTime;

        @NotNull
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime deletedAt;

        private MemberSimpleDTO member;
        private RoadmapDTO roadmap;
    }

    public static LayerResponse of(Layer layer) {
        LayerInfo layerInfo = new LayerInfo();
        layerInfo.setId(layer.getId());
        layerInfo.setName(layer.getName());
        layerInfo.setDescription(layer.getDescription());
        layerInfo.setLayerTime(layer.getLayerTime());
        layerInfo.setCreatedAt(layer.getCreatedAt());
        layerInfo.setUpdatedAt(layer.getUpdatedAt());
        layerInfo.setUpdatedAt(layer.getUpdatedAt());
        layerInfo.setDeletedAt(layer.getDeletedAt());
        layerInfo.setMember(new MemberSimpleDTO(layer.getMember()));
        layerInfo.setRoadmap(
            layer.getRoadmap() == null ? null : new RoadmapDTO(layer.getRoadmap()));

        return new LayerResponse(
            ResponseCode.OK.getCode(),
            "레이어 상세 조회 성공",
            LocalDateTime.now(),
            layerInfo
        );
    }

}

