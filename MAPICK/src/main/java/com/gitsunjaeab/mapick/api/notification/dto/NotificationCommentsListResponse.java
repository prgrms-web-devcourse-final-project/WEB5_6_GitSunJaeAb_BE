package com.gitsunjaeab.mapick.api.notification.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationCommentsListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<NotificationCommentsListDTO> notifications;

    // 공통 처리 로직
    public static NotificationCommentsListResponse of(List<Notification> notificationEntities) {
        return of(notificationEntities, "알림 목록 조회 성공");
    }

    // 생성 패턴용
    public static NotificationCommentsListResponse of(List<Notification> notificationEntities,
        String message) {
        List<NotificationCommentsListDTO> notificationCommentsListDTOs = notificationEntities.stream()
            .map(n -> {
                NotificationCommentsListDTO dto = new NotificationCommentsListDTO();
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

        return new NotificationCommentsListResponse(
            ResponseCode.OK.getCode(),
            message,
            LocalDateTime.now(),
            notificationCommentsListDTOs
        );

    }
}
