package com.gitsunjaeab.mapick.api.notification;

import com.gitsunjaeab.mapick.api.notification.dto.NotificationListDTO;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationListResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.NotificationReadResponse;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
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
        List<NotificationListDTO> dtoList = notificationService.findDtoByTypeAndMember(notificationType,
            memberId);
        String message = switch (notificationType) {
            case ANNOUNCEMENT -> "공지 알림 조회";
            case BOOKMARK -> "북마크 알림 조회";
            case QUEST -> "퀘스트 알림 조회";
            case COMMENT -> "댓글 알림 조회";
            case ZZIM -> "찜 알림 조회";
            case FORK -> "포크 알림 조회";
            default -> "전체 알림 조회 성공";
        };

        return ResponseEntity.ok(NotificationListResponse.success(dtoList, "알림 조회 성공"));
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
}
