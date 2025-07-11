package com.gitsunjaeab.mapick.api.common;

import java.time.LocalDateTime;

public interface BaseApiResponse {
    String getCode();
    String getMessage();
    LocalDateTime getTimestamp();
}

