package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerLibrary;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerZzimResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    // 찜한 사용자
    private MemberSimpleDTO memberSimpleDTO;

    // 찜한 레이어
    private LayerDTO layers;
    private OffsetDateTime deleteAt;

    private RoadmapSimpleDTO roadmaps;


    public static LayerZzimResponse of(LayerLibrary library, String message) {
        return new LayerZzimResponse(
            ResponseCode.OK.getCode(),
            message, // 응답 메시지 커스텀 반환
            OffsetDateTime.now(),
            new MemberSimpleDTO(library.getMember()),
            new LayerDTO(library.getLayer()),
            library.getDeletedAt(),
            new RoadmapSimpleDTO(library.getLayer().getRoadmap())
        );
    }
}

