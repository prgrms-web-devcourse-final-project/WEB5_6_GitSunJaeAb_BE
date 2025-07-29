package com.gitsunjaeab.mapick.api.search.dto.internal;

import com.gitsunjaeab.mapick.domain.search.Search;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class SearchHistoryDTO {

    private Long id;

    private String keyword;

    private Long memberId;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    public static SearchHistoryDTO of(Search search) {
        return SearchHistoryDTO.builder()
                .id(search.getId())
                .keyword(search.getKeyword())
                .memberId(search.getMember().getId())
                .createdAt(search.getCreatedAt())
                .updatedAt(search.getUpdatedAt())
                .deletedAt(search.getDeletedAt())
                .build();
    }


}
