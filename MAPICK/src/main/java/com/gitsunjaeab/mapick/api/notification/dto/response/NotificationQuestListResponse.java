package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.notification.dto.NotificationQuestListDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationQuestListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<NotificationQuestListDTO> notifications;

    // 공통 처리 로직
    public static NotificationQuestListResponse of(List<Notification> notificationEntities) {
        return of(notificationEntities, "알림 목록 조회 성공");
    }

    // 생성 패턴용
    public static NotificationQuestListResponse of(List<Notification> notificationEntities,
        String message) {
        List<NotificationQuestListDTO> notificationQuestListDTOs = notificationEntities.stream()
            .map(n -> {
                NotificationQuestListDTO dto = new NotificationQuestListDTO();
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

        return new NotificationQuestListResponse(
            ResponseCode.OK.getCode(),
            message,
            LocalDateTime.now(),
            notificationQuestListDTOs
        );

    }
}
