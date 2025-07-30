package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.response;

import com.gitsunjaeab.mapick.application.api.category.dto.CategorySimpleDTO;
import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.RoadmapListDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.HashtagDTO;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import java.time.OffsetDateTime;
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
    private OffsetDateTime timestamp;
    private List<RoadmapListDTO> roadmaps;

    // 데이터 포함 반환
    public static RoadmapListResponse of(
            List<Roadmap> roadmapEntities,
            Map<Long, Long> citationCountMap,
            Map<Long, Long> roadmapIdToBookmarkIdMap
    ) {
        List<RoadmapListDTO> roadmapDtos = roadmapEntities.stream()
                .map(r -> {
                    Long bookmarkId = roadmapIdToBookmarkIdMap.get(r.getId());
                    return new RoadmapListDTO(
                            r.getId(),
                            new CategorySimpleDTO(r.getCategory()),
                            new MemberSimpleDTO(r.getMember()),
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
                            citationCountMap.getOrDefault(r.getId(), 0L).intValue(),
                            r.getCreatedAt(),
                            bookmarkId != null,    // isBookmarked
                            bookmarkId             // bookmarkId 값 (있으면 포함)
                    );
                }).toList();

        return new RoadmapListResponse(
                ResponseCode.OK.getCode(),
                "로드맵 목록 조회 성공",
                OffsetDateTime.now(),
                roadmapDtos
        );
    }
}

