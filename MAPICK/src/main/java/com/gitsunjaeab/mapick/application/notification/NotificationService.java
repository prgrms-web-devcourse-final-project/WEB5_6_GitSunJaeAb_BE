package com.gitsunjaeab.mapick.application.notification;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import com.gitsunjaeab.mapick.domain.notification.NotificationRepository;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
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

    // 자동 알림 생성
    public Notification createNotification(
        Member target,
        NotificationType type,
        Roadmap roadmap,
        Layer layer,
        LayerLibrary layerLibrary,
        MemberQuest quest) {

        String title = "";
        String content = "";

        switch (type) {
            case FORK:
                title = "🍴포크 발생!";
                content = layer.getName() + "을(를) " + layerLibrary.getMember().getNickname() + "님이 인용했어요!";
                break;
            case POST:
                title = "🔥 로드맵 인기 급상승";
                content = roadmap.getTitle() + " 로드맵이 인기폭발이에요!";
                break;
            case QUEST:
                title = "🎯 퀘스트 참여";
                content = "퀘스트에 " + quest.getMember().getNickname() + "님이 참여했어요!";
                break;
            case COMMENT:
                title = "💬 새로운 댓글!";
                content = "내 로드맵 " + roadmap.getTitle() + "에 댓글이 달렸어요!";
                break;
            case ZZIM:
                title = "⭐ 찜 알림";
                content = layerLibrary.getMember().getNickname() + "님이 내 " + layer.getName() + "을(를) 찜했어요!";
                break;
            default:
                title = "🔔 기타 알림";
                content = "뭔가 새로운 일이 생겼어요!";
                break;
        }

        Notification notifications = Notification.builder()
            .title(title)
            .content(content)
            .member(target)
            .roadmap(roadmap)
            .layerLibrary(layerLibrary)
            .memberQuest(quest)
            .notificationType(type)
            .isRead(false)
            .createdAt(OffsetDateTime.now())
            .build();

        return notificationRepository.save(notifications);
    }

    // 커스텀 알림
    public Notification createCustomNotification(Member target, String title, String content, NotificationType type) {
        Notification notification = Notification.builder()
            .title(title)
            .content(content)
            .member(target)
            .notificationType(type)
            .isRead(false)
            .createdAt(OffsetDateTime.now())
            .build();
        return notificationRepository.save(notification);
    }

    // 알림 개별 읽음 처리
    @Transactional
    public void readNotification(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("알림이 존재하지 않습니다."));
        if (!notification.getMember().getId().equals(memberId)) {
            throw new RuntimeException("본인 알림만 읽음 처리할 수 있습니다.");
        }
        notification.setRead(true);
        notification.setReadAt(java.time.OffsetDateTime.now());
        notificationRepository.save(notification);
    }

    // ====== 읽음 후 1분 후 soft delete (테스트용) ======
    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    @Transactional
    public void deleteReadNotificationsAfterOneMinute() {
        OffsetDateTime oneMinuteAgo = OffsetDateTime.now().minusMinutes(1);
        List<Notification> notifications = notificationRepository.findByIsReadTrueAndReadAtBeforeAndDeletedAtIsNull(oneMinuteAgo);
        for (Notification n : notifications) {
            n.setDeletedAt(OffsetDateTime.now());
        }
        notificationRepository.saveAll(notifications);
    }

    // ====== 읽음 후 1일 후 soft delete (배포용, 주석처리) ======
    // @Scheduled(cron = "0 0 * * * *") // 매시 정각마다 실행
    // @Transactional
    // public void deleteReadNotificationsAfterOneDay() {
    //     OffsetDateTime oneDayAgo = OffsetDateTime.now().minusDays(1);
    //     List<Notification> notifications = notificationRepository.findByIsReadTrueAndReadAtBeforeAndDeletedAtIsNull(oneDayAgo);
    //     for (Notification n : notifications) {
    //         n.setDeletedAt(OffsetDateTime.now());
    //     }
    //     notificationRepository.saveAll(notifications);
    // }
}
