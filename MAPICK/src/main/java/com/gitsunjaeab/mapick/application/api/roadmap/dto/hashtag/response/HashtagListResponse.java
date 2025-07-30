package com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class HashtagListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<HashtagResponse> hashtags;

    public static HashtagListResponse of(List<Hashtag> hashtagEntities) {
        List<HashtagResponse> hashtagResponses = hashtagEntities.stream()
            .map(h -> {
                HashtagResponse dto = new HashtagResponse();
                dto.setId(h.getId());
                dto.setName(h.getName());
                dto.setCreatedAt(h.getCreatedAt());
                return dto;
            })
            .toList();

        return new HashtagListResponse(
            ResponseCode.OK.getCode(),
            "해시태그 목록 조회 성공",
            OffsetDateTime.now(),
            hashtagResponses
        );
    }
}
