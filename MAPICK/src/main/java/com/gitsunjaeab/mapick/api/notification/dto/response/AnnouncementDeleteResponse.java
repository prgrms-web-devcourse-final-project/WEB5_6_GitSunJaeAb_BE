package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementDeleteResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    // 공지 삭제 성공 응답 생성용
    public static AnnouncementDeleteResponse of(String message) {
        return new AnnouncementDeleteResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now()
        );
    }
} 