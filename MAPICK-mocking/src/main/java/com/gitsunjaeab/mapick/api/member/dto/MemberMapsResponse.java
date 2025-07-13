package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class MemberMapsResponse {

    private String message;
    private List<MapInfo> maps;

    @Getter
    @Setter
    public static class MapInfo {
        private Long id;
        private String title;
        private String description;
        private String thumbnail;
        private Boolean isPublic;
        private Boolean isAnimated;
        private Integer likeCount;
        private Integer viewCount;
        private String mapType;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }

    public static MemberMapsResponse of(List<Roadmap> roadmaps) {
        MemberMapsResponse response = new MemberMapsResponse();
        response.setMessage("사용자 지도 목록 조회 성공");
        
        List<MapInfo> mapInfos = roadmaps.stream()
            .map(roadmap -> {
                MapInfo mapInfo = new MapInfo();
                mapInfo.setId(roadmap.getId());
                mapInfo.setTitle(roadmap.getTitle());
                mapInfo.setDescription(roadmap.getDescription());
                mapInfo.setThumbnail(roadmap.getThumbnail());
                mapInfo.setIsPublic(roadmap.getIsPublic());
                mapInfo.setIsAnimated(roadmap.getIsAnimated());
                mapInfo.setLikeCount(roadmap.getLikeCount());
                mapInfo.setViewCount(roadmap.getViewCount());
                mapInfo.setMapType(roadmap.getRoadmapType().name());
                mapInfo.setCreatedAt(roadmap.getCreatedAt());
                mapInfo.setUpdatedAt(roadmap.getUpdatedAt());
                return mapInfo;
            })
            .collect(Collectors.toList());
        
        response.setMaps(mapInfos);
        return response;
    }
} 