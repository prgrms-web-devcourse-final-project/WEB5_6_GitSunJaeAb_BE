package com.gitsunjaeab.mapick.api.roadmap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.api.member.dto.MemberProfileResponse.MemberInfo;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RoadmapResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private RoadmapInfo roadmap;

    public RoadmapResponse(String code, String message, LocalDateTime timestamp, RoadmapInfo roadmap){
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

        private String description;

        @Size(max = 255)
        private String thumbnail;

        private MemberSimpleDTO member;

        @NotNull
        @JsonProperty("isPublic")
        private Boolean isPublic;

        @NotNull
        @JsonProperty("isAnimated")
        private Boolean isAnimated;

        private Integer likeCount;

        private Integer viewCount;
    }

    // --- 정적 팩토리 메서드 ---
    public static RoadmapResponse of(Roadmap r) {
        RoadmapInfo roadmapInfo = new RoadmapInfo();
        roadmapInfo.setTitle(r.getTitle());
        roadmapInfo.setDescription(r.getDescription());
        roadmapInfo.setThumbnail(r.getThumbnail());
        roadmapInfo.setMember(new MemberSimpleDTO(r.getMember()));
        roadmapInfo.setIsPublic(r.getIsPublic());
        roadmapInfo.setIsAnimated(r.getIsAnimated());
        roadmapInfo.setLikeCount(r.getLikeCount());
        roadmapInfo.setViewCount(r.getViewCount());

        return new RoadmapResponse(
            ResponseCode.OK.getCode(),
            "로드맵 조회 성공",
            LocalDateTime.now(),
            roadmapInfo
        );
    }
}