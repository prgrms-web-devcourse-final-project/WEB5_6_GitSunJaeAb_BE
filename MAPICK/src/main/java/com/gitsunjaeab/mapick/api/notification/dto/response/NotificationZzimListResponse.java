package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.notification.dto.NotificationZzimListDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationZzimListResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<NotificationZzimListDTO> notifications;

    // 공통 처리 로직
    public static NotificationZzimListResponse of(List<Notification> notificationEntities) {
        return of(notificationEntities, "알림 목록 조회 성공");
    }

    // 생성 패턴용
    public static NotificationZzimListResponse of(List<Notification> notificationEntities,
        String message) {
        List<NotificationZzimListDTO> notificationZzimListDTOs = notificationEntities.stream()
            .map(n -> {
                NotificationZzimListDTO dto = new NotificationZzimListDTO();
                dto.setId(n.getId());
                dto.setTitle(n.getTitle());
                dto.setContent(n.getContent());
                
                // 수신자 정보 (알림을 받는 사용자)
                dto.setReceiver(n.getMember() != null ? new MemberSimpleDTO(n.getMember()) : null);
                
                // 발신자 정보 (알림을 발생시킨 사용자) - 찜 알림의 경우 LayerLibrary에서 발신자 정보 가져오기
                MemberSimpleDTO sender = null;
                if (n.getLayerLibrary() != null && n.getLayerLibrary().getMember() != null) {
                    sender = new MemberSimpleDTO(n.getLayerLibrary().getMember());
                }
                dto.setSender(sender);
                
                dto.setCreatedAt(n.getCreatedAt());
                dto.setUpdatedAt(n.getUpdatedAt());
                dto.setDeletedAt(n.getDeletedAt());
                dto.setReadAt(n.getReadAt());
                dto.setNotificationType(n.getNotificationType());
                dto.setAnnouncementType(n.getAnnouncementType());
                dto.setRead(n.isRead());
                
                // 알림 타입에 따른 관련 정보 매핑 (구조화된 DTO 사용)
                dto.setRelatedRoadmap(n.getRoadmap() != null ? new RoadmapSimpleDTO(n.getRoadmap()) : null);
                dto.setRelatedLayer(n.getLayerLibrary() != null && n.getLayerLibrary().getLayer() != null ?
                    new LayerSimpleDTO(n.getLayerLibrary().getLayer()) : null);
                dto.setRelatedQuestId(n.getMemberQuest() != null ? n.getMemberQuest().getId() : null);
                dto.setRelatedCommentId(null); // 댓글 관련 알림은 별도 처리 필요
                
                return dto;
            })
            .toList();

        return new NotificationZzimListResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            notificationZzimListDTOs
        );

    }
}
