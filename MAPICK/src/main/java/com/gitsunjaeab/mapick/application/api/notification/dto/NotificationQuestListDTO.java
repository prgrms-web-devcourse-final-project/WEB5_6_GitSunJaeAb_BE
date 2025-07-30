package com.gitsunjaeab.mapick.application.api.notification.dto;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.notification.code.AnnouncementType;
import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQuestListDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String content;

    private MemberSimpleDTO member;

    private boolean isRead;

    private AnnouncementType announcementType;

    private NotificationType notificationType;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private OffsetDateTime readAt;

    private Long questId;

}
