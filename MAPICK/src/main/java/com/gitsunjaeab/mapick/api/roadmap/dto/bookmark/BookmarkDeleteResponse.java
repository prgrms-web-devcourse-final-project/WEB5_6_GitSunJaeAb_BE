package com.gitsunjaeab.mapick.api.roadmap.dto.bookmark;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookmarkDeleteResponse implements BaseApiResponse {

    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    public static BookmarkDeleteResponse of(ResponseCode responseCode, String message) {
        return new BookmarkDeleteResponse(
                responseCode.getCode(),
                message,
                LocalDateTime.now()
        );
    }
}
