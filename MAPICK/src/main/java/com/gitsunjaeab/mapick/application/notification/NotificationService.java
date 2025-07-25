package com.gitsunjaeab.mapick.application.notification;

import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import com.gitsunjaeab.mapick.domain.notification.NotificationRepository;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    // ===== 기본 CRUD =====

    // 타입 조회
    public List<Notification> findByType(NotificationType type) {
        // 전체
        if (type == NotificationType.ALL) {
            // fetch join으로 N+1 문제 방지
            return notificationRepository.findAllWithAllRelations();
        }
        // 타입별 조회도 fetch join + deletedAt 조건 쿼리 사용
        return notificationRepository.findByNotificationTypeWithAllRelations(type);
    }

    // 타입+멤버 조회 (본인 알림만)
    public List<Notification> findByTypeAndMember(NotificationType type, Long memberId) {
        if (type == NotificationType.ALL) {
            return notificationRepository.findAllWithAllRelationsByMemberId(memberId);
        }
        return notificationRepository.findByNotificationTypeWithAllRelationsAndMemberId(type,
            memberId);
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
            case POST:
                title = "🔖 로드맵 북마크 알림";
                content = "'" + bookmark.getMember().getNickname() + "' 님이 내 '" + roadmap.getTitle()
                    + "' 로드맵을 북마크 했어요!";
                break;
            case QUEST:
                title = "🎯 퀘스트 참여";
                content = "내 퀘스트에 '" + quest.getMember().getNickname() + "'님이 참여했어요!";
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

        return notificationRepository.save(notifications);
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

    // ====== 읽음 후 1분 후 soft delete (테스트용) ======
    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    @Transactional
    public void deleteReadNotificationsAfterOneMinute() {
        OffsetDateTime oneMinuteAgo = OffsetDateTime.now().minusMinutes(1);
        List<Notification> notifications = notificationRepository.findReadBeforeAndNotDeletedWithAllRelations(
            oneMinuteAgo);
        for (Notification n : notifications) {
            n.setDeletedAt(OffsetDateTime.now());
        }
        notificationRepository.saveAll(notifications);
    }

    // ====== 읽음 후 1일 후 soft delete (배포용, 주석처리) ======
//     @Scheduled(cron = "0 0 * * * *") // 매시 정각마다 실행
//     @Transactional
//     public void deleteReadNotificationsAfterOneDay() {
//         OffsetDateTime oneDayAgo = OffsetDateTime.now().minusDays(1);
//         List<Notification> notifications = notificationRepository.findReadBeforeAndNotDeletedWithAllRelations(oneDayAgo);
//         for (Notification n : notifications) {
//             n.setDeletedAt(OffsetDateTime.now());
//         }
//         notificationRepository.saveAll(notifications);
//     }
}
