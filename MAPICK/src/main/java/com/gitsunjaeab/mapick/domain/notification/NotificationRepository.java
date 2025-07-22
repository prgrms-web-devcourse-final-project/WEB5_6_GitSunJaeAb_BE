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
}
