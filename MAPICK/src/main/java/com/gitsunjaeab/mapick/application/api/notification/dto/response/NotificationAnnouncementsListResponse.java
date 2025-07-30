package com.gitsunjaeab.mapick.application.api.notification.dto.response;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.notification.dto.NotificationAnnouncementsListDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.notification.Notification;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationAnnouncementsListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<NotificationAnnouncementsListDTO> notifications;

    // 공통 처리 로직
    public static NotificationAnnouncementsListResponse of(List<Notification> notificationEntities) {
        return of(notificationEntities, "알림 목록 조회 성공");
    }

    // 생성 패턴용
    public static NotificationAnnouncementsListResponse of(List<Notification> notificationEntities,
        String message) {
        List<NotificationAnnouncementsListDTO> notificationAnnouncementsListDTOs = notificationEntities.stream()
            .map(n -> {
                NotificationAnnouncementsListDTO dto = new NotificationAnnouncementsListDTO();
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

        return new NotificationAnnouncementsListResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            notificationAnnouncementsListDTOs
        );

    }
}
