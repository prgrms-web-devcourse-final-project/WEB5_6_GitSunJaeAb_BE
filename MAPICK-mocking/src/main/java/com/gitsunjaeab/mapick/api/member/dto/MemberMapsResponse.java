package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class MemberMapsResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 멤버 지도 목록 데이터
    private List<MapInfo> maps;

    public MemberMapsResponse(String code, String message, LocalDateTime timestamp, List<MapInfo> maps) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.maps = maps;
    }

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

        return new MemberMapsResponse(
            ResponseCode.OK.getCode(),
            "회원 지도 조회 성공",
            LocalDateTime.now(),
            mapInfos
        );
    }
} 