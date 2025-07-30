package com.gitsunjaeab.mapick.infra.common.response;

import java.time.OffsetDateTime;

public interface BaseApiResponse {
    String getCode();
    String getMessage();
    OffsetDateTime getTimestamp();
}

