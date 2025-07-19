package com.gitsunjaeab.mapick.api.roadmap.dto.bookmark;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookmarkCreateResponse implements BaseApiResponse {

    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    public static BookmarkCreateResponse of(ResponseCode responseCode, String message) {
        return new BookmarkCreateResponse(
                responseCode.getCode(),
                message,
                LocalDateTime.now()
        );
    }
}