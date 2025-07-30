package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.api.notification.dto.NotificationSimpleDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationReadResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<NotificationSimpleDTO> notification;

    // 단일 공지도 배열로 감싸서 반환
    public static NotificationReadResponse of(Notification notification, String message) {
        return new NotificationReadResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            List.of(NotificationSimpleDTO.from(notification))
        );
    }
}

