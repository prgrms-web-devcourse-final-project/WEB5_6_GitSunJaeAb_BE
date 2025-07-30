package com.gitsunjaeab.mapick.application.api.roadmap.dto.bookmark;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class BookmarkCreateResponse implements BaseApiResponse {

    private final String code;
    private final String message;
    private final OffsetDateTime timestamp;
    private final Long bookmarkId;

    public static BookmarkCreateResponse of(ResponseCode responseCode, String message, Long bookmarkId) {
        return new BookmarkCreateResponse(
                responseCode.getCode(),
                message,
                OffsetDateTime.now(),
                bookmarkId
        );
    }

    public BookmarkCreateResponse(String code, String message, OffsetDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.bookmarkId = null;
    }
}