package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.LayerDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerForkResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    // 포크한 사용자
    private MemberSimpleDTO member;

    // 포크한 원본
    private LayerDTO layers;

    // 포크된 내 레이어, 로드맵
    private Long forkedLayerId;
    private RoadmapSimpleDTO forkedRoadmap;

    private OffsetDateTime deleteAt;

    public static LayerForkResponse fork(
        LayerLibrary originalLibrary,
        Layer forkedLayer,
        Roadmap targetRoadmap,
        String message) {
        return new LayerForkResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            new MemberSimpleDTO(originalLibrary.getMember()),
            new LayerDTO(originalLibrary.getLayer()),
            forkedLayer.getId(),
            new RoadmapSimpleDTO(targetRoadmap),
            originalLibrary.getDeletedAt());
    }
}

