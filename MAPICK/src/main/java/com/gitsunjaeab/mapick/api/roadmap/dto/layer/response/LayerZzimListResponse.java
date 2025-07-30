package com.gitsunjaeab.mapick.api.roadmap.dto.layer.response;

import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerForkHistoryDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerForkHistory;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LayerZzimListResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    // 찜한 사용자
    private MemberSimpleDTO member;

    // 찜한 레이어 (포크 이력 포함) - DTO 없이 직접 처리
    private List<LayerWithForkHistoryDTO> layers;


    @Getter
    @AllArgsConstructor
    public static class LayerWithForkHistoryDTO {

        private Long id;
        private String name;
        private String description;
        private Integer layerSeq;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime deletedAt;
        private MemberSimpleDTO member;
        private RoadmapSimpleDTO roadmap;
        private List<LayerForkHistoryDTO> forkHistories;
        private List<MarkerSimpleDTO> markers;
    }

    // 찜한레이어 리스트 + 포크이력 + 마커정보
    public static LayerZzimListResponse of(
        Member loginMember,
        List<Layer> layers,
        Map<Long, List<LayerForkHistory>> forkHistoriesMap,
        String message) {

        // 찜한 레이어가 없을 경우
        if (layers.isEmpty()) {
            return new LayerZzimListResponse(
                ResponseCode.OK.getCode(),
                "찜한 레이어 없음",
                OffsetDateTime.now(),
                new MemberSimpleDTO(loginMember),
                List.of()
            );
        }

        // 레이어별로 프크 이력 DTO 로 변환해서 묶기
        List<LayerWithForkHistoryDTO> layerDTOs = layers.stream()
            .map(layer -> {
                List<LayerForkHistoryDTO> forkHistoryDTOs = forkHistoriesMap
                    .getOrDefault(layer.getId(), List.of())
                    .stream()
                    .map(LayerForkHistoryDTO::from)
                    .toList();

                List<MarkerSimpleDTO> markerDTOs = layer.getLayerMarkers().stream()
                    .map(MarkerSimpleDTO::from)
                    .toList();

                return new LayerWithForkHistoryDTO(
                    layer.getId(),
                    layer.getName(),
                    layer.getDescription(),
                    layer.getLayerSeq(),
                    layer.getCreatedAt(),
                    layer.getUpdatedAt(),
                    layer.getDeletedAt(),
                    new MemberSimpleDTO(layer.getMember()),
                    new RoadmapSimpleDTO(layer.getRoadmap()),
                    forkHistoryDTOs,
                    markerDTOs
                );
            })
            .toList();

        // 최종 응답 객체 반환
        return new LayerZzimListResponse(
            ResponseCode.OK.getCode(),
            message, // 커스텀 메시지 사용
            OffsetDateTime.now(),
            new MemberSimpleDTO(loginMember),
            layerDTOs);
    }

    // 단순 조회시 사용
    public static LayerZzimListResponse of(
        Member loginMember,
        List<Layer> layers,
        Map<Long, List<LayerForkHistory>> forkHistoriesMap) {
        return of(loginMember, layers, forkHistoriesMap, "찜한 레이어 조회 성공");
    }


    public static LayerZzimListResponse of(LayerZzimSimpleDTO dto, String message) {
        List<LayerWithForkHistoryDTO> layerDTOs = dto.getLayers().stream()
            .map(layerSimpleDTO -> {
                List<LayerForkHistoryDTO> forkHistoryDTOs = dto.getForkHistoriesMap()
                    .getOrDefault(layerSimpleDTO.getId(), List.of())
                    .stream()
                    .map(LayerForkHistoryDTO::from)
                    .toList();

                List<MarkerSimpleDTO> markerDTOs = dto.getMarkers().stream()
                    .filter(m -> m.getLayerId().equals(layerSimpleDTO.getId()))
                    .toList();

                return new LayerWithForkHistoryDTO(
                    layerSimpleDTO.getId(),
                    layerSimpleDTO.getName(),
                    layerSimpleDTO.getDescription(),
                    layerSimpleDTO.getLayerSeq(),
                    layerSimpleDTO.getCreatedAt(),
                    null, // updatedAt
                    null, // deletedAt
                    null, // MemberSimpleDTO
                    null, // RoadmapSimpleDTO
                    forkHistoryDTOs,
                    markerDTOs
                );
            }).toList();

        return new LayerZzimListResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            new MemberSimpleDTO(dto.getMember()),
            layerDTOs
        );
    }
}

