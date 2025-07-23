package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarkerListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<MarkerListDTO> markers;

    public static MarkerListResponse of(List<Marker> markerEntities) {
        List<MarkerListDTO> markerDTOs = markerEntities.stream()
            .map(m -> new MarkerListDTO(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getAddress(),
                m.getLat(),
                m.getLng(),
                m.getColor(),
                m.getMarkerImage(),
                m.getMarkerSeq(),
                m.getLayer() != null ? m.getLayer().getId() : null,
                new MemberSimpleDTO(m.getMember())
            ))
            .toList();

        return new MarkerListResponse(
            ResponseCode.OK.getCode(),
            "마커 목록 조회 성공",
            LocalDateTime.now(),
            markerDTOs
        );
    }
}

