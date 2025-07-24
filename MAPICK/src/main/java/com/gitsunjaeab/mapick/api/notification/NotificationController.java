package com.gitsunjaeab.mapick.api.notification;

import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationAnnouncementsListResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationCommentsListResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationForkListResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationListResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationPostsListResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationQuestListResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationReadResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationZzimListResponse;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "알림 관리 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "알림 조회", description = "[사용자용] 알림 전체 또는 유형별 조회")
    public ResponseEntity<? extends BaseApiResponse> findNotifications(
        @AuthenticationPrincipal Principal principal,
        @RequestParam(defaultValue = "ALL") NotificationType notificationType
    ) {
        Long memberId = principal.getMember().getId();
        List<Notification> notifications = notificationService.findByTypeAndMember(notificationType,
            memberId);

        return switch (notificationType) {
            case ANNOUNCEMENT -> ResponseEntity.ok(
                NotificationAnnouncementsListResponse.of(notifications, "공지 알림 조회"));
            case POST ->
                ResponseEntity.ok(NotificationPostsListResponse.of(notifications, "게시글 알림 조회"));
            case QUEST ->
                ResponseEntity.ok(NotificationQuestListResponse.of(notifications, "퀘스트 알림 조회"));
            case COMMENT ->
                ResponseEntity.ok(NotificationCommentsListResponse.of(notifications, "댓글 알림 조회"));
            case ZZIM ->
                ResponseEntity.ok(NotificationZzimListResponse.of(notifications, "찜 알림 조회"));
            case FORK ->
                ResponseEntity.ok(NotificationForkListResponse.of(notifications, "포크 알림 조회"));

            default -> ResponseEntity.ok(NotificationListResponse.of(notifications, "전체 알림 조회 성공"));
        };
    }

    // 알림 개별 읽음 처리
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationReadResponse> readNotification(
        @AuthenticationPrincipal Principal principal,
        @PathVariable Long id
    ) {
        Long memberId = principal.getMember().getId();
        Notification notification = notificationService.readNotification(id, memberId);
        return ResponseEntity.ok(NotificationReadResponse.of(notification, "알림을 읽었습니다.(test 시 1분 후 삭제됨)"));
    }

//    // 알림 조회 - 전체
//    @GetMapping
//    @Operation(summary = "알림 조회 - 전체", description = "[사용자용] 모달에서 전체 알림 조회")
//    public ResponseEntity<NotificationListResponse> findAllNotification() {
//        final List<Notification> notifications = notificationService.findByType(NotificationType.All);
//        return ResponseEntity.ok(NotificationListResponse.of(notifications, "전체 알림 조회 성공"));
//    }
//
//    // 알림 조회 - 공지
//    @GetMapping("/announcements")
//    @Operation(summary = "알림 조회 - 공지", description = "[사용자용] 모달에서 공지 알림 조회")
//    public ResponseEntity<NotificationAnnouncementsListResponse> findAnnouncementNotification() {
//        final List<Notification> notifications = notificationService.findByType(NotificationType.Announcement);
//        return ResponseEntity.ok(NotificationAnnouncementsListResponse.of(notifications, "공지 알림 조회 성공"));
//    }
//
//    // 알림 조회 - 게시글
//    @GetMapping("/posts")
//    @Operation(summary = "알림 조회 - 게시글", description = "[사용자용] 모달에서 게시글 관련 알림 조회")
//    public ResponseEntity<NotificationPostsListResponse> findPostNotification() {
//        final List<Notification> notifications = notificationService.findByType(NotificationType.Post);
//        return ResponseEntity.ok(NotificationPostsListResponse.of(notifications, "게시글 알림 조회 성공"));
//    }
//
//    // 알림 조회 - 퀘스트
//    @GetMapping("/quests")
//    @Operation(summary = "알림 조회 - 퀘스트", description = "[사용자용] 모달에서 퀘스트 알림 조회")
//    public ResponseEntity<NotificationQuestListResponse> findQuestNotification() {
//        final List<Notification> notifications = notificationService.findByType(NotificationType.Quest);
//        return ResponseEntity.ok(NotificationQuestListResponse.of(notifications, "퀘스트 알림 조회 성공"));
//    }
//
//    // 알림 조회 - 댓글
//    @GetMapping("/comments")
//    @Operation(summary = "알림 조회 - 댓글", description = "[사용자용] 모달에서 댓글 알림 조회")
//    public ResponseEntity<NotificationCommentsListResponse> findCommentsNotification() {
//        final List<Notification> notifications = notificationService.findByType(NotificationType.Comment);
//        return ResponseEntity.ok(NotificationCommentsListResponse.of(notifications, "댓글 알림 조회 성공"));
//    }
//
//    // 알림 조회 - 찜
//    @GetMapping("/zzim")
//    @Operation(summary = "알림 조회 - 찜", description = "[사용자용] 모달에서 찜 알림 조회")
//    public ResponseEntity<NotificationZzimListResponse> findZzimNotification() {
//        final List<Notification> notifications = notificationService.findByType(NotificationType.Zzim);
//        return ResponseEntity.ok(NotificationZzimListResponse.of(notifications, "찜 알림 조회 성공"));
//    }
//
//    // 알림 조회 - 포크(인용)
//    @GetMapping("/fork")
//    @Operation(summary = "알림 조회 - 포크", description = "[사용자용] 모달에서 포크(인용) 알림 조회")
//    public ResponseEntity<NotificationForkListResponse> findForkNotification() {
//        final List<Notification> notifications = notificationService.findByType(NotificationType.Fork);
//        return ResponseEntity.ok(NotificationForkListResponse.of(notifications, "포크 알림 조회 성공"));
//    }
}
