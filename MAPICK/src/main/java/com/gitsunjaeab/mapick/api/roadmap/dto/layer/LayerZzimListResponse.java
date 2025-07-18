package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerZzimListResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 찜한 사용자
    private MemberSimpleDTO memberSimpleDTO;

    // 찜한 레이어
    private List<LayerDTO> layers;


    public static LayerZzimListResponse of(Member loginMember, List<Layer> layers) {
        if (layers.isEmpty()) {
            return new LayerZzimListResponse(
                ResponseCode.OK.getCode(),
                "찜한 레이어 없음",
                LocalDateTime.now(),
                new MemberSimpleDTO(loginMember),
                List.of()
            );
        }

        List<LayerDTO> layerDTOList = layers.stream()
            .map(LayerDTO::new)
            .toList();

        return new LayerZzimListResponse(
            ResponseCode.OK.getCode(),
            "찜한 레이어 조회 성공",
            LocalDateTime.now(),
            new MemberSimpleDTO(loginMember),
            layerDTOList
        );
    }
}

