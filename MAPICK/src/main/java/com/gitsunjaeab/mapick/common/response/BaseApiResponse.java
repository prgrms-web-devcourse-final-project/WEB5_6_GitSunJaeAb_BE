package com.gitsunjaeab.mapick.common.response;

import java.time.OffsetDateTime;

public interface BaseApiResponse {
    String getCode();
    String getMessage();
    OffsetDateTime getTimestamp();
}

