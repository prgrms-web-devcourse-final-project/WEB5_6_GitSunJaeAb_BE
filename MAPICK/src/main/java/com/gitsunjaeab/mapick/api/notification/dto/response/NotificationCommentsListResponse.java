package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.api.comment.dto.CommentSimpleDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.notification.dto.NotificationCommentsListDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapSimpleDTO;
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
    private List<RoadmapSimpleDTO> roadmaps;
    private List<QuestSimpleDTO> quests;

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
                // 댓글 정보(내용, 작성자 등)
                dto.setComment(n.getComment() != null ? new CommentSimpleDTO(n.getComment()) : null);
                // 수신인(알림 받는 사람)
                dto.setReceiver(n.getMember() != null ? new MemberSimpleDTO(n.getMember()) : null);
                dto.setCreatedAt(n.getCreatedAt());
                dto.setUpdatedAt(n.getUpdatedAt());
                dto.setDeletedAt(n.getDeletedAt());
                dto.setReadAt(n.getReadAt());
                dto.setNotificationType(n.getNotificationType());
                dto.setAnnouncementType(n.getAnnouncementType());
                dto.setRead(n.isRead());
                // 연관관계 정보
                dto.setQuest(n.getQuest() != null ? new QuestSimpleDTO(n.getQuest()) : null);
                dto.setRoadmap(n.getRoadmap() != null ? new RoadmapSimpleDTO(n.getRoadmap()) : null);
                return dto;
            })
            .toList();

        return new NotificationCommentsListResponse(
            ResponseCode.OK.getCode(),
            message,
            LocalDateTime.now(),
            notificationCommentsListDTOs,
            null,
            null
        );

    }
}
