package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.api.notification.dto.NotificationSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationReadResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<NotificationSimpleDTO> notification;

    // 단일 공지도 배열로 감싸서 반환
    public static NotificationReadResponse of(Notification notification, String message) {
        return new NotificationReadResponse(
            ResponseCode.OK.getCode(),
            message,
            LocalDateTime.now(),
            List.of(NotificationSimpleDTO.from(notification))
        );
    }
}

