package com.gitsunjaeab.mapick.api.notification;

import com.gitsunjaeab.mapick.api.notification.dto.request.AnnouncementCreateRequest;
import com.gitsunjaeab.mapick.api.notification.dto.response.AnnouncementResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.AnnouncementDeleteResponse;
import com.gitsunjaeab.mapick.api.notification.dto.response.AnnouncementListResponse;
import com.gitsunjaeab.mapick.application.notification.AdminAnnouncementService;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.notification.Announcement;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/announcements")
@RequiredArgsConstructor
@Tag(name = "공지 관리 API")
public class AdminAnnouncementController {

    private final AdminAnnouncementService announcementService;

    // 공지 때리기
    @PostMapping
    @Operation(summary = "관리자 공지 생성", description = "[관리자] 공지 생성")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
        @AuthenticationPrincipal Principal principal,
        @RequestBody AnnouncementCreateRequest request
    ){
        if (principal == null) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        Long memberId = principal.getMember().getId();
        Announcement announcement = announcementService.createAnnouncement(memberId, request);
        return ResponseEntity.ok(AnnouncementResponse.of(announcement, "관리자 공지 생성 성공"));
    }

    // 공지 수정
    @PutMapping("/{id}")
    @Operation(summary = "관리자 공지 수정", description = "[관리자] 공지 수정")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
        @AuthenticationPrincipal Principal principal,
        @PathVariable Long id,
        @RequestBody AnnouncementCreateRequest request
    ) {
        if (principal == null) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }
        Long memberId = principal.getMember().getId();
        Announcement announcement = announcementService.updateAnnouncement(memberId, id, request);
        return ResponseEntity.ok(AnnouncementResponse.of(announcement, "관리자 공지 수정 성공"));
    }

    // 공지 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "관리자 공지 삭제", description = "[관리자] 공지 삭제")
    public ResponseEntity<AnnouncementDeleteResponse> deleteAnnouncement(
        @AuthenticationPrincipal Principal principal,
        @PathVariable Long id
    ) {
        if (principal == null) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }
        Long memberId = principal.getMember().getId();
        announcementService.deleteAnnouncement(memberId, id);
        return ResponseEntity.ok(AnnouncementDeleteResponse.of("공지 삭제 성공"));
    }

    // 공지 조회
    @GetMapping
    @Operation(summary = "관리자 공지 조회", description = "[사용자 및 관리자] 공지 조회")
    public ResponseEntity<AnnouncementListResponse> getAnnouncements() {
        List<Announcement> announcements = announcementService.findAll();
        return ResponseEntity.ok(AnnouncementListResponse.of(announcements, "공지 목록 조회 성공"));
    }

}
