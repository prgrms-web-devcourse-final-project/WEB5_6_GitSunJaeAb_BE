package com.gitsunjaeab.mapick.api.roadmap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoadmapResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    // --- 실제 데이터 필드 ---
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

    // --- 정적 팩토리 메서드 ---
    public static RoadmapResponse of(Roadmap r) {
        return new RoadmapResponse(
            ResponseCode.OK.getCode(),
            "로드맵 조회 성공",
            LocalDateTime.now(),
            r.getTitle(),
            r.getDescription(),
            r.getThumbnail(),
            new MemberSimpleDTO(r.getMember()),
            r.getIsPublic(),
            r.getIsAnimated(),
            r.getLikeCount(),
            r.getViewCount()
        );
    }
}