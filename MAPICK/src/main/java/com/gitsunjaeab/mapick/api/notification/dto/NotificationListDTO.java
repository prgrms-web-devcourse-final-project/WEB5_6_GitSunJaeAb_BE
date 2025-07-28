package com.gitsunjaeab.mapick.api.notification.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerSimpleDTO;
import com.gitsunjaeab.mapick.domain.notification.AnnouncementType;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
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
public class NotificationListDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String content;

    // 기존 member 필드는 수신자(receiver)로 변경
    private MemberSimpleDTO receiver;  // 알림을 받는 사용자 (수신자)

    // 새로운 발신자 필드 추가
    private MemberSimpleDTO sender;    // 알림을 발생시킨 사용자 (발신자)

    private boolean isRead;

    private AnnouncementType announcementType;

    private NotificationType notificationType;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private OffsetDateTime readAt;

    // 알림 타입에 따른 관련 정보 (구조화된 DTO로 변경)
    private RoadmapSimpleDTO relatedRoadmap;    // 관련 로드맵 정보
    private LayerSimpleDTO relatedLayer;        // 관련 레이어 정보
    private Long relatedQuestId;                // 관련 퀘스트 ID
    private Long relatedCommentId;              // 관련 댓글 ID


    public static NotificationListDTO of(Notification n) {
        NotificationListDTO dto = new NotificationListDTO();
        dto.setId(n.getId());
        dto.setTitle(n.getTitle());
        dto.setContent(n.getContent());
        dto.setReceiver(n.getMember() != null ? new MemberSimpleDTO(n.getMember()) : null);

        MemberSimpleDTO sender = null;
        if (n.getLayerLibrary() != null && n.getLayerLibrary().getMember() != null) {
            sender = new MemberSimpleDTO(n.getLayerLibrary().getMember());
        } else if (n.getMemberQuest() != null && n.getMemberQuest().getMember() != null) {
            sender = new MemberSimpleDTO(n.getMemberQuest().getMember());
        }
        dto.setSender(sender);

        dto.setCreatedAt(n.getCreatedAt());
        dto.setUpdatedAt(n.getUpdatedAt());
        dto.setDeletedAt(n.getDeletedAt());
        dto.setReadAt(n.getReadAt());
        dto.setNotificationType(n.getNotificationType());
        dto.setAnnouncementType(n.getAnnouncementType());
        dto.setRead(n.isRead());

        dto.setRelatedRoadmap(n.getRoadmap() != null ? new RoadmapSimpleDTO(n.getRoadmap()) : null);
        dto.setRelatedLayer(n.getLayerLibrary() != null && n.getLayerLibrary().getLayer() != null ?
            new LayerSimpleDTO(n.getLayerLibrary().getLayer()) : null);
        dto.setRelatedQuestId(n.getMemberQuest() != null ? n.getMemberQuest().getId() : null);
        dto.setRelatedCommentId(null); // 필요 시 추가

        return dto;
    }

}
