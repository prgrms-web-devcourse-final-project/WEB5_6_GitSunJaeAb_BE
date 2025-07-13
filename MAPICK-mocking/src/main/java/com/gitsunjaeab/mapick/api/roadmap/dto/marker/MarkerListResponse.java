package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MarkerListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<MarkerListRequest> markers;

    public static MarkerListResponse of(List<Marker> markerEntities) {
        List<MarkerListRequest> markerDTOs = markerEntities.stream()
            .map(m -> new MarkerListRequest(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getLat(),
                m.getLng(),
                m.getColor(),
                m.getImageUrl(),
                m.getMarkerSeq(),
                m.getLayer() != null ? m.getLayer().getId() : null
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

