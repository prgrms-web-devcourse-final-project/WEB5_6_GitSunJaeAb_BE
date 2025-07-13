package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class MemberLayersResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 멤버 레이어 라이브러리 목록 데이터
    private List<LayerLibraryInfo> layerLibraries;

    public MemberLayersResponse(String code, String message, LocalDateTime timestamp, List<LayerLibraryInfo> layerLibraries) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.layerLibraries = layerLibraries;
    }

    @Getter
    @Setter
    public static class LayerLibraryInfo {
        private Long layerLibraryId;
        private Long memberId;
        private Long layerId;
        private OffsetDateTime createdAt;
        private LayerInfo layer;
    }

    @Getter
    @Setter
    public static class LayerInfo {
        private Long id;
        private String name;
        private String description;
        private Integer layerSeq;
        private LocalDate layerTime;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }

    public static MemberLayersResponse of(List<LayerLibrary> layerLibraries) {
        List<LayerLibraryInfo> layerLibraryInfos = layerLibraries.stream()
            .map(layerLibrary -> {
                LayerLibraryInfo layerLibraryInfo = new LayerLibraryInfo();
                layerLibraryInfo.setLayerLibraryId(layerLibrary.getId());
                layerLibraryInfo.setMemberId(layerLibrary.getMember().getId());
                layerLibraryInfo.setLayerId(layerLibrary.getLayer().getId());
                layerLibraryInfo.setCreatedAt(layerLibrary.getCreatedAt());

                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setId(layerLibrary.getLayer().getId());
                layerInfo.setName(layerLibrary.getLayer().getName());
                layerInfo.setDescription(layerLibrary.getLayer().getDescription());
                layerInfo.setLayerSeq(layerLibrary.getLayer().getLayerSeq());
                layerInfo.setLayerTime(layerLibrary.getLayer().getLayerTime());
                layerInfo.setCreatedAt(layerLibrary.getLayer().getCreatedAt());
                layerInfo.setUpdatedAt(layerLibrary.getLayer().getUpdatedAt());

                layerLibraryInfo.setLayer(layerInfo);
                return layerLibraryInfo;
            })
            .collect(Collectors.toList());

        return new MemberLayersResponse(
            ResponseCode.OK.getCode(),
            "회원 레이어 라이브러리 조회 성공",
            LocalDateTime.now(),
            layerLibraryInfos
        );
    }
} 