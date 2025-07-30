package com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;
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
    private MemberSimpleDTO member;

    // 찜한 레이어
    private LayerDTO layers;
    private OffsetDateTime deleteAt;

    public static LayerZzimResponse zzim(LayerLibrary library, String message) {
        return new LayerZzimResponse(
            ResponseCode.OK.getCode(),
            message, // 응답 메시지 커스텀 반환
            OffsetDateTime.now(),
            new MemberSimpleDTO(library.getMember()),
            new LayerDTO(library.getLayer()),
            library.getDeletedAt()
        );
    }

    public static LayerZzimResponse removeZzim(LayerLibrary library, String message) {
        return new LayerZzimResponse(
            ResponseCode.OK.getCode(),
            message, // 응답 메시지 커스텀 반환
            OffsetDateTime.now(),
            new MemberSimpleDTO(library.getMember()),
            new LayerDTO(library.getLayer()),
            library.getDeletedAt()
        );
    }
}

