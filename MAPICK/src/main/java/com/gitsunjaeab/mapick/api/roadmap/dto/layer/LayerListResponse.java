package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@AllArgsConstructor
public class LayerListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<LayerListDTO> layers;



    // 공통 처리 로직
    public static LayerListResponse of(List<Layer> layerEntities) {
        List<LayerListDTO> layerDTOs = layerEntities.stream()
            .map(l -> {
                LayerListDTO dto = new LayerListDTO();
                dto.setId(l.getId());
                dto.setName(l.getName());
                dto.setDescription(l.getDescription());
                dto.setLayerSeq(l.getLayerSeq());
                dto.setLayerTime(l.getLayerTime());
                dto.setCreatedAt(l.getCreatedAt());
                dto.setMember(l.getMember() != null ? new MemberSimpleDTO(l.getMember()) : null);
                dto.setRoadmap(l.getRoadmap() != null ? l.getRoadmap().getId() : null);
                return dto;
            })
            .toList();

        return new LayerListResponse(
            ResponseCode.OK.getCode(),
            "레이어 목록 조회 성공",
            LocalDateTime.now(),
            layerDTOs
        );
    }
}

