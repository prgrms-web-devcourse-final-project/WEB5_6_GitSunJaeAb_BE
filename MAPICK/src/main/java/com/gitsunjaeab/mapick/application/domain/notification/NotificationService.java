package com.gitsunjaeab.mapick.application.domain.notification;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.notification.dto.internal.NotificationListDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.internal.MarkerSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.comment.Comment;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.application.domain.quest.Quest;
import com.gitsunjaeab.mapick.application.domain.roadmap.bookmark.Bookmark;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // ===== 기본 CRUD =====

    // 엔티티 반환, 타입+멤버 조회 (본인 알림만)
    public List<Notification> findByTypeAndMember(NotificationType type, Long memberId) {
        if (type == NotificationType.ALL) {
            return notificationRepository.findAllWithAllRelationsByMemberId(memberId);
        } else {
            return notificationRepository.findByNotificationTypeWithAllRelationsAndMemberId(type, memberId);
        }
    }

    // DTO 변환
    @Transactional(readOnly = true)
    public List<NotificationListDTO> findDtoByTypeAndMember(NotificationType type, Long memberId) {
        List<Notification> notifications = findByTypeAndMember(type, memberId);
        return mapToListDTO(notifications);
    }

    // 자동 알림 생성
    @Transactional
    public Notification createNotification(
        Member target,
        NotificationType type,
        Roadmap roadmap,
        Layer layer,
        LayerLibrary layerLibrary,
        Quest quest,
        MemberQuest memberQuest,
        Comment comment,
        Bookmark bookmark
    ) {

        String title = ""; // 알림 제목
        String content = ""; // 알림 설명

        switch (type) {
            case FORK:
                title = "🍴포크 발생!";
                content = layerLibrary.getMember().getNickname()
                    + "님이 " + layer.getName() + "을(를) 인용했어요!";
                break;
            case BOOKMARK:
                if (roadmap != null) {
                    title = "🔖 로드맵 북마크 알림";
                    content = "'" + bookmark.getMember().getNickname() + "' 님이 내 '" + roadmap.getTitle() + "' 로드맵을 북마크 했어요!";
                }
                break;
            case QUEST:
                title = "🧭 퀘스트 참여 알림";
                content = "'" + memberQuest.getMember().getNickname() +"'님의 '" + memberQuest.getQuest().getTitle() + "' 퀘스트에 참여했습니다!";
                break;
            case QUEST_DEADLINE:
                title = "🧭 퀘스트 마감 알림";
                content = "'" + memberQuest.getMember().getNickname() +"'님의 '" + memberQuest.getQuest().getTitle() + "' 퀘스트의 마감 1분 전 입니다!"; // 테스트용
//                content = "'" + memberQuest.getMember().getNickname() +"'님의 '" + memberQuest.getQuest().getTitle() + "' 퀘스트의 마감 D-1 일 전 입니다!"; // 배포용
                break;
            case COMMENT:
                if (roadmap != null && roadmap.getTitle() != null) {
                    title = "💬 로드맵 댓글 알림";
                    content = "내 로드맵 '" + roadmap.getTitle() + "'에 '" + comment.getMember().getNickname() + "'님이 댓글을 남겼어요!";
                } else if (quest != null && quest.getTitle() != null) {
                    title = "💬 퀘스트 댓글 알림";
                    content = "내 퀘스트 '" + quest.getTitle() + "'에 '" + comment.getMember().getNickname() + "'님이 댓글을 남겼어요!";
                }
                break;
            case ZZIM:
                title = "⭐ 찜 알림";
                content = layerLibrary.getMember().getNickname() + "님이 내 " + layer.getName()
                    + "을(를) 찜했어요!";
                break;
            default:
                title = "🔔 기타 알림";
                content = "뭔가 새로운 일이 생겼어요!";
                break;
        }

        Notification notifications = Notification.builder()
            .title(title)
            .content(content)
            .comment(comment)
            .member(target)
            .roadmap(roadmap)
            .layerLibrary(layerLibrary)
            .quest(quest)
            .memberQuest(memberQuest)
            .bookmark(bookmark)
            .notificationType(type)
            .isRead(false)
            .createdAt(OffsetDateTime.now())
            .build();

        if (title.isEmpty() || content.isEmpty()) {
            log.warn("알림 : title 또는 content 비어있음. 저장 생략함");

            return null;
        }

        return notificationRepository.save(notifications);
    }

    public List<NotificationListDTO> mapToListDTO(List<Notification> notificationEntities) {
        return notificationEntities.stream()
            .map(n -> {
                NotificationListDTO dto = new NotificationListDTO();
                dto.setId(n.getId());
                dto.setTitle(n.getTitle());
                dto.setContent(n.getContent());

                dto.setReceiver(n.getMember() != null ? new MemberSimpleDTO(n.getMember()) : null);

                MemberSimpleDTO sender = null;
                if (n.getLayerLibrary() != null && n.getLayerLibrary().getMember() != null) {
                    sender = new MemberSimpleDTO(n.getLayerLibrary().getMember());
                } else if (n.getQuest() != null && n.getQuest().getMember() != null) {
                    sender = new MemberSimpleDTO(n.getQuest().getMember());
                } else if (n.getComment() != null && n.getComment().getMember() != null) {
                    sender = new MemberSimpleDTO(n.getComment().getMember());
                } else if (n.getLayerForkHistory() != null && n.getLayerForkHistory().getMember() != null) {
                    sender = new MemberSimpleDTO(n.getLayerForkHistory().getMember());
                } else if (n.getAnnouncement() != null && n.getAnnouncement().getMember() != null) {
                    sender = new MemberSimpleDTO(n.getAnnouncement().getMember());
                } else if (n.getBookmark() != null && n.getBookmark().getMember() != null) {
                    sender = new MemberSimpleDTO(n.getBookmark().getMember());
                }
                dto.setSender(
                    n.getSenderMember() != null ? new MemberSimpleDTO(n.getSenderMember()) : null
                );

                dto.setCreatedAt(n.getCreatedAt());
                dto.setUpdatedAt(n.getUpdatedAt());
                dto.setDeletedAt(n.getDeletedAt());
                dto.setReadAt(n.getReadAt());
                dto.setNotificationType(n.getNotificationType());
                dto.setAnnouncementType(n.getAnnouncementType());
                dto.setRead(n.isRead());

                dto.setRelatedRoadmap(n.getRoadmap() != null ? new RoadmapSimpleDTO(n.getRoadmap()) : null);
                dto.setRelatedLayer(n.getLayerLibrary() != null && n.getLayerLibrary().getLayer() != null
                    ? new LayerSimpleDTO(n.getLayerLibrary().getLayer()) : null);
                // 퀘스트 ID 설정 - MemberQuest 또는 Quest에서 가져오기
                Long questId = null;
                if (n.getMemberQuest() != null) {
                    questId = n.getMemberQuest().getQuest().getId();
                } else if (n.getQuest() != null) {
                    questId = n.getQuest().getId();
                }
                dto.setRelatedQuestId(questId);
                // 댓글 ID 설정 - 댓글 알림인 경우 해당 댓글 ID 설정
                dto.setRelatedCommentId(n.getComment() != null ? n.getComment().getId() : null);

                if (n.getLayerLibrary() != null && n.getLayerLibrary().getLayer() != null) {
                    List<MarkerSimpleDTO> markers = n.getLayerLibrary().getLayer().getLayerMarkers().stream()
                        .map(MarkerSimpleDTO::from)
                        .toList();
                    dto.setRelatedMarkers(markers);
                }

                return dto;
            })
            .toList();
    }


    // 알림 개별 읽음 처리
    @Transactional
    public Notification readNotification(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findByIdWithAllRelations(notificationId)
            .orElseThrow(() -> new RuntimeException("알림이 존재하지 않습니다."));
        if (!notification.getMember().getId().equals(memberId)) {
            throw new RuntimeException("본인 알림만 읽음 처리할 수 있습니다.");
        }
        notification.setRead(true);
        notification.setReadAt(java.time.OffsetDateTime.now());
        return notificationRepository.save(notification);
    }
}
