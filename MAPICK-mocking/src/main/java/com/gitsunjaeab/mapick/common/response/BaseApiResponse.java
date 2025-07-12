package com.gitsunjaeab.mapick.common.response;

import java.time.LocalDateTime;

public interface BaseApiResponse {
    String getCode();
    String getMessage();
    LocalDateTime getTimestamp();
}

