package com.gitsunjaeab.mapick.api.roadmap.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로드맵 목록(List) 반환 Response
 */
@Getter
@AllArgsConstructor
public class RoadmapListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<RoadmapListDTO> roadmaps;

    // 데이터 포함 반환
    public static RoadmapListResponse of(List<Roadmap> roadmapEntities, Map<Long, Long> citationCountMap) {
        List<RoadmapListDTO> roadmapDtos = roadmapEntities.stream()
            .map(r -> new RoadmapListDTO(
                r.getId(),
                r.getCategory().getName(),
                r.getTitle(),
                r.getDescription(),
                r.getThumbnail(),
                r.getRoadmapMapHashtags().stream()
                    .map(h -> new HashtagDTO(h.getHashtag().getId(), h.getHashtag().getName()))
                    .collect(Collectors.toList()),
                r.getIsPublic(),
                r.getRoadmapType(),
                r.getLikeCount(),
                r.getViewCount(),
                citationCountMap.getOrDefault(r.getId(), 0L).intValue()
            )).toList();

        return new RoadmapListResponse(
            ResponseCode.OK.getCode(),
            "로드맵 목록 조회 성공",
            LocalDateTime.now(),
            roadmapDtos
        );
    }
}

