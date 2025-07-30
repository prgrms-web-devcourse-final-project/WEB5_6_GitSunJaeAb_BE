package com.gitsunjaeab.mapick.application.api.notification.dto.internal;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.notification.Notification;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSimpleDTO {

    private Long id; // 알림 ID
    private String title; // 알림 제목
    private String content; // 알림 내용
    private OffsetDateTime createdAt; // 생성일
    private MemberSimpleDTO member; // 작성자 정보
    private boolean isRead; // 읽음 여부

    public static NotificationSimpleDTO from(Notification notification) {
        return new NotificationSimpleDTO(
            notification.getId(),
            notification.getTitle(),
            notification.getContent(),
            notification.getCreatedAt(),
            MemberSimpleDTO.of(notification.getMember()),
            notification.isRead()
        );
    }
}
