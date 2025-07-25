package com.gitsunjaeab.mapick.infra.scheduler;

import com.gitsunjaeab.mapick.application.quest.MemberQuestService;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import com.gitsunjaeab.mapick.domain.notification.NotificationRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class DeadlineScheduler {

    private final MemberQuestService memberQuestService;
    private final NotificationRepository notificationRepository;

    // 스케쥴러 - 멤버퀘스트 마감 알림 발송
    @Scheduled(cron = "0 * * * * *") //
    public void sendQuestDeadlineNotification() {
        memberQuestService.sendDeadlineNotification();
    }

    // 스케쥴러 - 알림 읽음 후 1분 후 soft delete (테스트용)
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

    // 스케쥴러 - 알림 읽음 후 1분 후 soft delete (배포용, 주석처리) ======
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
