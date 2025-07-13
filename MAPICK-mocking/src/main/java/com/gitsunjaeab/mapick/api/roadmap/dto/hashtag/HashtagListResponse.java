package com.gitsunjaeab.mapick.api.roadmap.dto.hashtag;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.roadmap.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class HashtagListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
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
            LocalDateTime.now(),
            hashtagResponses
        );
    }
}
