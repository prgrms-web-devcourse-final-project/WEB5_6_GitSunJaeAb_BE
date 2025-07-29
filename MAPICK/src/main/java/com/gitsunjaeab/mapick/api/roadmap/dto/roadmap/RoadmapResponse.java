package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.api.category.dto.CategorySimpleDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerWithMarkerDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RoadmapResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private RoadmapInfo roadmap;

    public RoadmapResponse(String code, String message, OffsetDateTime timestamp, RoadmapInfo roadmap){
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.roadmap = roadmap;
    }

    @Getter
    @Setter
    public static class RoadmapInfo {
        @NotNull
        @Size(max = 255)
        private String title;

        private CategorySimpleDTO category;

        private String description;

        @Size(max = 255)
        private String thumbnail;

        private MemberSimpleDTO member;
        
        private List<HashtagDTO> hashtags;

        @NotNull
        @JsonProperty("isPublic")
        private Boolean isPublic;

        @NotNull
        @JsonProperty("isAnimated")
        private Boolean isAnimated;

        private Integer likeCount;

        private Integer viewCount;

        private List<LayerWithMarkerDTO> layers;
        private String address;
        private Double regionLatitude;
        private Double regionLongitude;
        private OffsetDateTime participationEnd;
    }

    // --- 정적 팩토리 메서드 ---
    public static RoadmapResponse of(Roadmap r) {
        RoadmapInfo roadmapInfo = new RoadmapInfo();
        roadmapInfo.setTitle(r.getTitle());
        roadmapInfo.setCategory(new CategorySimpleDTO(r.getCategory()));
        roadmapInfo.setDescription(r.getDescription());
        roadmapInfo.setThumbnail(r.getThumbnail());
        roadmapInfo.setMember(new MemberSimpleDTO(r.getMember()));
        List<HashtagDTO> hashtags = r.getRoadmapMapHashtags().stream()
            .map(h -> new HashtagDTO(h.getHashtag().getId(), h.getHashtag().getName()))
            .collect(Collectors.toList());
        roadmapInfo.setHashtags(hashtags);
        roadmapInfo.setIsPublic(r.getIsPublic());
        roadmapInfo.setIsAnimated(r.getIsAnimated());
        roadmapInfo.setLikeCount(r.getLikeCount());
        roadmapInfo.setViewCount(r.getViewCount());

        List<LayerWithMarkerDTO> layers = r.getRoadmapLayers().stream()
                .filter(layer -> layer.getDeletedAt() == null)
                .map(LayerWithMarkerDTO::new)
                .collect(Collectors.toList());
        roadmapInfo.setLayers(layers);
        roadmapInfo.setAddress(r.getAddress());
        roadmapInfo.setRegionLatitude(r.getRegionLatitude());
        roadmapInfo.setRegionLongitude(r.getRegionLongitude());
        roadmapInfo.setParticipationEnd(r.getParticipationEnd());

        return new RoadmapResponse(
            ResponseCode.OK.getCode(),
            "로드맵 조회 성공",
            OffsetDateTime.now(),
            roadmapInfo
        );
    }
}