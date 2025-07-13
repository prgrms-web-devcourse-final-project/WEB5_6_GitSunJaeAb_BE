package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class MemberLayersResponse {

    private String message;
    private List<LayerLibraryInfo> layerLibraries;

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
        MemberLayersResponse response = new MemberLayersResponse();
        response.setMessage("레이어 목록 조회 성공");
        
        List<LayerLibraryInfo> layerLibraryInfos = layerLibraries.stream()
            .map(layerLibrary -> {
                LayerLibraryInfo info = new LayerLibraryInfo();
                info.setLayerLibraryId(layerLibrary.getId());
                info.setMemberId(layerLibrary.getMember().getId());
                info.setLayerId(layerLibrary.getLayer().getId());
                info.setCreatedAt(layerLibrary.getCreatedAt());
                
                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setId(layerLibrary.getLayer().getId());
                layerInfo.setName(layerLibrary.getLayer().getName());
                layerInfo.setDescription(layerLibrary.getLayer().getDescription());
                layerInfo.setLayerSeq(layerLibrary.getLayer().getLayerSeq());
                layerInfo.setLayerTime(layerLibrary.getLayer().getLayerTime());
                layerInfo.setCreatedAt(layerLibrary.getLayer().getCreatedAt());
                layerInfo.setUpdatedAt(layerLibrary.getLayer().getUpdatedAt());
                
                info.setLayer(layerInfo);
                return info;
            })
            .collect(Collectors.toList());
        
        response.setLayerLibraries(layerLibraryInfos);
        return response;
    }
} 