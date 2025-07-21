package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerForkHistory;
import java.time.LocalDateTime;
import java.time.LocalDate;
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
    private LocalDateTime timestamp;

    // 찜한 사용자
    private MemberSimpleDTO memberSimpleDTO;

    // 찜한 레이어 (포크 이력 포함) - DTO 없이 직접 처리
    private List<LayerWithForkHistoryDTO> layers;

    @Getter
    @AllArgsConstructor
    public static class LayerWithForkHistoryDTO {
        private Long id;
        private String name;
        private String description;
        private Integer layerSeq;
        private LocalDate layerTime;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime deletedAt;
        private MemberSimpleDTO member;
        private RoadmapSimpleDTO roadmap;
        private List<LayerForkHistoryDTO> forkHistories;
    }

    public static LayerZzimListResponse of(Member loginMember, List<Layer> layers, Map<Long, List<LayerForkHistory>> forkHistoriesMap) {
        return of(loginMember, layers, forkHistoriesMap, "찜한 레이어 조회 성공");
    }

    public static LayerZzimListResponse of(LayerZzimSimpleDTO layerZzimSimpleDTO, String message) {
        return of(layerZzimSimpleDTO.getMember(), layerZzimSimpleDTO.getLayers(), layerZzimSimpleDTO.getForkHistoriesMap(), message);
    }

    public static LayerZzimListResponse of(Member loginMember, List<Layer> layers, Map<Long, List<LayerForkHistory>> forkHistoriesMap, String message) {
        if (layers.isEmpty()) {
            return new LayerZzimListResponse(
                ResponseCode.OK.getCode(),
                "찜한 레이어 없음",
                LocalDateTime.now(),
                new MemberSimpleDTO(loginMember),
                List.of()
            );
        }

        List<LayerWithForkHistoryDTO> layerDTOs = layers.stream()
            .map(layer -> {
                List<LayerForkHistoryDTO> forkHistoryDTOs = forkHistoriesMap
                    .getOrDefault(layer.getId(), List.of())
                    .stream()
                    .map(LayerForkHistoryDTO::from)
                    .toList();

                return new LayerWithForkHistoryDTO(
                    layer.getId(),
                    layer.getName(),
                    layer.getDescription(),
                    layer.getLayerSeq(),
                    layer.getLayerTime(),
                    layer.getCreatedAt(),
                    layer.getUpdatedAt(),
                    layer.getDeletedAt(),
                    new MemberSimpleDTO(layer.getMember()),
                    new RoadmapSimpleDTO(layer.getRoadmap()),
                    forkHistoryDTOs
                );
            })
            .toList();

        return new LayerZzimListResponse(
            ResponseCode.OK.getCode(),
            message, // 커스텀 메시지 사용
            LocalDateTime.now(),
            new MemberSimpleDTO(loginMember),
            layerDTOs
        );
    }
}

