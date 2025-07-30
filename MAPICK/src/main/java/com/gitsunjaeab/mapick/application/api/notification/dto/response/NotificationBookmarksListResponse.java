package com.gitsunjaeab.mapick.application.api.notification.dto.response;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.notification.dto.NotificationBookmarksListDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.notification.Notification;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class NotificationBookmarksListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<NotificationBookmarksListDTO> notifications;

    // 공통 처리 로직
    public static NotificationBookmarksListResponse of(List<Notification> notificationEntities) {
        return of(notificationEntities, "알림 목록 조회 성공");
    }

    // 생성 패턴용
    public static NotificationBookmarksListResponse of(List<Notification> notificationEntities,
        String message) {
        List<NotificationBookmarksListDTO> notificationBookmarksListDTOS = notificationEntities.stream()
            .map(n -> {
                NotificationBookmarksListDTO dto = new NotificationBookmarksListDTO();
                dto.setId(n.getId());
                dto.setTitle(n.getTitle());
                dto.setContent(n.getContent());
                dto.setMember(n.getMember() != null ? new MemberSimpleDTO(n.getMember()) : null);
                dto.setCreatedAt(n.getCreatedAt());
                dto.setUpdatedAt(n.getUpdatedAt());
                dto.setDeletedAt(n.getDeletedAt());
                dto.setReadAt(n.getReadAt());
                dto.setNotificationType(n.getNotificationType());
                dto.setAnnouncementType(n.getAnnouncementType());
                dto.setRead(n.isRead());
                return dto;
            })
            .toList();

        return new NotificationBookmarksListResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            notificationBookmarksListDTOS
        );

    }
}
