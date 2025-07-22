package com.gitsunjaeab.mapick.api.notification.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerSimpleDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.notification.AnnouncementType;
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

}
