package com.gitsunjaeab.mapick.application.domain.notification;

import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    // ===== 기본 CRUD =====

    // 알림 전체를 연관 엔티티와 함께 fetch join으로 조회 (N+1 문제 방지, 삭제된 알림 제외)
    @Query("SELECT n FROM Notification n " +
        "LEFT JOIN FETCH n.member " +
        "LEFT JOIN FETCH n.comment c " +
        "LEFT JOIN FETCH c.member " +
        "LEFT JOIN FETCH n.roadmap " +
        "LEFT JOIN FETCH n.layerLibrary " +
        "LEFT JOIN FETCH n.quest " +
        "LEFT JOIN FETCH n.memberQuest mq " +
        "LEFT JOIN FETCH mq.quest q " +
        "WHERE n.deletedAt IS NULL " +
        "ORDER BY n.createdAt DESC")
    List<Notification> findAllWithAllRelations();

    // 특정 공지(announcementId)와 연관된 Notification 모두 조회
   List<Notification> findByAnnouncementId(Long announcementId);

    // 특정 공지(announcementId)와 연관된 Notification 모두 조회
    // 알림 전체를 연관 엔티티와 함께 fetch join으로 조회 (N+1 문제 방지, 삭제된 알림 제외)
    @Query("SELECT n FROM Notification n " +
        "LEFT JOIN FETCH n.member " +
        "LEFT JOIN FETCH n.comment c " +
        "LEFT JOIN FETCH c.member " +
        "LEFT JOIN FETCH n.roadmap " +
        "LEFT JOIN FETCH n.layerLibrary " +
        "LEFT JOIN FETCH n.quest " +
        "LEFT JOIN FETCH n.memberQuest " +
        "WHERE n.deletedAt IS NULL AND n.notificationType = :type " +
        "ORDER BY n.createdAt DESC")
    List<Notification> findByNotificationTypeWithAllRelations(NotificationType type);

    // 읽음 처리된 알림 중 readAt이 before보다 이전이고 deletedAt이 null인 알림 조회
    List<Notification> findByIsReadTrueAndReadAtBeforeAndDeletedAtIsNull(
        OffsetDateTime before);

    // 본인(memberId) 알림 전체 fetch join (삭제된 알림 제외)
    @Query("""
            SELECT n FROM Notification n
            LEFT JOIN FETCH n.member m
            LEFT JOIN FETCH n.comment c
            LEFT JOIN FETCH c.member
            LEFT JOIN FETCH n.roadmap
            LEFT JOIN FETCH n.layerLibrary ll
            LEFT JOIN FETCH ll.layer
            LEFT JOIN FETCH ll.member
            LEFT JOIN FETCH n.quest
            LEFT JOIN FETCH n.memberQuest mq
            LEFT JOIN FETCH mq.quest q
            LEFT JOIN FETCH n.bookmark b
            LEFT JOIN FETCH b.member
            WHERE n.deletedAt IS NULL AND n.member.id = :memberId
            ORDER BY n.createdAt DESC
        """)
    List<Notification> findAllWithAllRelationsByMemberId(@Param("memberId") Long memberId);

    // 본인(memberId) 알림 타입별 fetch join (삭제된 알림 제외)
    @Query("SELECT n FROM Notification n " +
        "LEFT JOIN FETCH n.member " +
        "LEFT JOIN FETCH n.comment c " +
        "LEFT JOIN FETCH c.member " +
        "LEFT JOIN FETCH n.roadmap " +
        "LEFT JOIN FETCH n.layerLibrary " +
        "LEFT JOIN FETCH n.quest " +
        "LEFT JOIN FETCH n.memberQuest " +
        "WHERE n.deletedAt IS NULL AND n.notificationType = :type AND n.member.id = :memberId " +
        "ORDER BY n.createdAt DESC")
    List<Notification> findByNotificationTypeWithAllRelationsAndMemberId(
        NotificationType type, Long memberId);


    // ===== 읽음 처리 =====

    @Query("SELECT n FROM Notification n " +
        "LEFT JOIN FETCH n.member " +
        "LEFT JOIN FETCH n.comment " +
        "LEFT JOIN FETCH n.roadmap " +
        "LEFT JOIN FETCH n.layerLibrary ll " +
        "LEFT JOIN FETCH ll.member " +
        "LEFT JOIN FETCH ll.layer " +
        "LEFT JOIN FETCH n.quest " +
        "LEFT JOIN FETCH n.memberQuest mq " +
        "LEFT JOIN FETCH mq.member " +
        "WHERE n.id = :id")
    Optional<Notification> findByIdWithAllRelations(Long id);

    @Query("SELECT n FROM Notification n " +
        "LEFT JOIN FETCH n.member " +
        "LEFT JOIN FETCH n.comment " +
        "LEFT JOIN FETCH n.roadmap " +
        "LEFT JOIN FETCH n.layerLibrary ll " +
        "LEFT JOIN FETCH ll.member " +
        "LEFT JOIN FETCH ll.layer " +
        "LEFT JOIN FETCH n.quest " +
        "LEFT JOIN FETCH n.memberQuest mq " +
        "LEFT JOIN FETCH mq.member " +
        "WHERE n.isRead = true AND n.readAt < :before AND n.deletedAt IS NULL")
    List<Notification> findReadBeforeAndNotDeletedWithAllRelations(OffsetDateTime before);



    //    @Query("SELECT n FROM Notification n " +
//        "LEFT JOIN FETCH n.member " +
// "LEFT JOIN FETCH n.comment " +
//        "LEFT JOIN FETCH n.roadmap " +
//        "LEFT JOIN FETCH n.layerLibrary ll " +
//        "LEFT JOIN FETCH ll.member " +
//        "LEFT JOIN FETCH ll.layer " +
//    "LEFT JOIN FETCH n.quest " +
//        "LEFT JOIN FETCH n.memberQuest mq " +
//        "LEFT JOIN FETCH mq.member " +
//        "WHERE n.notificationType = :type " +
//        "ORDER BY n.createdAt DESC")
//    List<Notification> findByNotificationTypeOrderByCreatedAtDesc(NotificationType type);
//
//    @Query("SELECT n FROM Notification n " +
//        "LEFT JOIN FETCH n.member " +
// "LEFT JOIN FETCH n.comment " +
//        "LEFT JOIN FETCH n.roadmap " +
//        "LEFT JOIN FETCH n.layerLibrary ll " +
//        "LEFT JOIN FETCH ll.member " +
//        "LEFT JOIN FETCH ll.layer " +
//    "LEFT JOIN FETCH n.quest " +
//        "LEFT JOIN FETCH n.memberQuest mq " +
//        "LEFT JOIN FETCH mq.member " +
//        "ORDER BY n.createdAt DESC")
//    List<Notification> findAllByOrderByCreatedAtDesc();
//
}
