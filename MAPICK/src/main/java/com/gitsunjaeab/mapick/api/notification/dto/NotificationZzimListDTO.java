package com.gitsunjaeab.mapick.api.notification.dto;

import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerSimpleDTO;
import com.gitsunjaeab.mapick.domain.notification.AnnouncementType;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationZzimListDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String content;

    // 수신자 정보 (알림을 받는 사용자)
    private MemberSimpleDTO receiver;

    // 발신자 정보 (알림을 발생시킨 사용자)
    private MemberSimpleDTO sender;

    private boolean isRead;

    private AnnouncementType announcementType;

    private NotificationType notificationType;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private OffsetDateTime readAt;

    // 알림 타입에 따른 관련 정보 (구조화된 DTO 사용)
    private RoadmapSimpleDTO relatedRoadmap;    // 관련 로드맵 정보
    private LayerSimpleDTO relatedLayer;        // 관련 레이어 정보
    private Long relatedQuestId;                // 관련 퀘스트 ID
    private Long relatedCommentId;              // 관련 댓글 ID

}
