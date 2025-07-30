package com.gitsunjaeab.mapick.application.api.roadmap.dto.bookmark.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class BookmarkDeleteResponse implements BaseApiResponse {

    private final String code;
    private final String message;
    private final OffsetDateTime timestamp;

    public static BookmarkDeleteResponse of(ResponseCode responseCode, String message) {
        return new BookmarkDeleteResponse(
                responseCode.getCode(),
                message,
                OffsetDateTime.now()
        );
    }
}
