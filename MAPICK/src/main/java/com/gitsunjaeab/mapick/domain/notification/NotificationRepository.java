package com.gitsunjaeab.mapick.domain.notification;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n " +
           "LEFT JOIN FETCH n.member " +
           "LEFT JOIN FETCH n.roadmap " +
           "LEFT JOIN FETCH n.layerLibrary ll " +
           "LEFT JOIN FETCH ll.member " +
           "LEFT JOIN FETCH ll.layer " +
           "LEFT JOIN FETCH n.memberQuest mq " +
           "LEFT JOIN FETCH mq.member " +
           "WHERE n.notificationType = :type " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findByNotificationTypeOrderByCreatedAtDesc(NotificationType type);

    @Query("SELECT n FROM Notification n " +
           "LEFT JOIN FETCH n.member " +
           "LEFT JOIN FETCH n.roadmap " +
           "LEFT JOIN FETCH n.layerLibrary ll " +
           "LEFT JOIN FETCH ll.member " +
           "LEFT JOIN FETCH ll.layer " +
           "LEFT JOIN FETCH n.memberQuest mq " +
           "LEFT JOIN FETCH mq.member " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findAllByOrderByCreatedAtDesc();

    // 알림 전체를 연관 엔티티와 함께 fetch join으로 조회 (N+1 문제 방지, 삭제된 알림 제외)
    @org.springframework.data.jpa.repository.Query("SELECT n FROM Notification n " +
        "LEFT JOIN FETCH n.member " +
        "LEFT JOIN FETCH n.roadmap " +
        "LEFT JOIN FETCH n.layerLibrary " +
        "LEFT JOIN FETCH n.memberQuest " +
        "WHERE n.deletedAt IS NULL " +
        "ORDER BY n.createdAt DESC")
    java.util.List<Notification> findAllWithAllRelations();

    // 특정 공지(announcementId)와 연관된 Notification 모두 조회
    java.util.List<Notification> findByAnnouncementId(Long announcementId);

    // 특정 공지(announcementId)와 연관된 Notification 모두 조회
    // 알림 전체를 연관 엔티티와 함께 fetch join으로 조회 (N+1 문제 방지, 삭제된 알림 제외)
    @Query("SELECT n FROM Notification n " +
       "LEFT JOIN FETCH n.member " +
       "LEFT JOIN FETCH n.roadmap " +
       "LEFT JOIN FETCH n.layerLibrary " +
       "LEFT JOIN FETCH n.memberQuest " +
       "WHERE n.deletedAt IS NULL AND n.notificationType = :type " +
       "ORDER BY n.createdAt DESC")
    List<Notification> findByNotificationTypeWithAllRelations(NotificationType type);
}
