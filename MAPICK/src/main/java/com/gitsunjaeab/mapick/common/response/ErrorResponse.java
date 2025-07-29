package com.gitsunjaeab.mapick.common.response;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
}
