package com.gitsunjaeab.mapick.application.notification;

import com.gitsunjaeab.mapick.api.notification.dto.request.AnnouncementCreateRequest;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Role;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.notification.Announcement;
import com.gitsunjaeab.mapick.domain.notification.AdminAnnouncementRepository;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import com.gitsunjaeab.mapick.domain.notification.NotificationRepository;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAnnouncementService {

    private final MemberRepository memberRepository;
    private final AdminAnnouncementRepository adminAnnouncementRepository;
    private final NotificationRepository notificationRepository;


    // 공지 때리기
    @PreAuthorize("hasRole('ADMIN')")
    public Announcement createAnnouncement(Long memberId, AnnouncementCreateRequest request) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        if (!member.hasRole(Role.ROLE_ADMIN)) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        Announcement announcement = Announcement.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .announcementType(request.getAnnouncementType())
            .member(member)
            .build();
        announcement = adminAnnouncementRepository.save(announcement);
        final Announcement announcementEntity = announcement;
        // 전체 member에게 공지 알림(Notification) 발송
        List<Member> members = memberRepository.findAll();
        List<Notification> notifications = members.stream()
            .map(m -> Notification.builder()
                .title(announcementEntity.getTitle())
                .content(announcementEntity.getContent())
                .member(m) // 알림 수신자
                .announcementType(announcementEntity.getAnnouncementType())
                .notificationType(NotificationType.ANNOUNCEMENT)
                .createdAt(java.time.OffsetDateTime.now())
                .announcement(announcementEntity) // 공지 연관관계 세팅
                .build())
            .toList();
        notificationRepository.saveAll(notifications);

        return announcement;
    }

    // 수정
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Announcement updateAnnouncement(Long memberId, Long announcementId, AnnouncementCreateRequest request) {
        // 관리자 멤버 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
        if (!member.hasRole(Role.ROLE_ADMIN)) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }
        Announcement announcement = adminAnnouncementRepository.findAllWithMember().stream()
            .filter(a -> a.getId().equals(announcementId))
            .findFirst()
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        // 제목, 내용 수정
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setAnnouncementType(request.getAnnouncementType());
        return adminAnnouncementRepository.save(announcement);
    }

    // 삭제
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAnnouncement(Long memberId, Long announcementId) {
        // 관리자 멤버 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
        if (!member.hasRole(Role.ROLE_ADMIN)) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }
        Announcement announcement = adminAnnouncementRepository.findAllWithMember().stream()
            .filter(a -> a.getId().equals(announcementId))
            .findFirst()
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        // soft delete: 삭제 시각만 기록
        announcement.setDeletedAt(java.time.OffsetDateTime.now());
        adminAnnouncementRepository.save(announcement);

        // 연관된 공지 알림(Notification)도 soft delete 처리
        List<Notification> notifications = notificationRepository.findByAnnouncementId(announcementId);
        java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
        for (Notification n : notifications) {
            n.setDeletedAt(now);
        }
        notificationRepository.saveAll(notifications);
    }

    // 조회
    @Transactional(readOnly = true)
    public List<Announcement> findAll() {
        return adminAnnouncementRepository.findAllWithMember();
    }

    // 공지 상세 조회
    public Announcement findById(Long id) {
        Announcement announcement = adminAnnouncementRepository.findByIdWithMember(id)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        if (announcement.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.NOT_FOUND);
        }
        return announcement;
    }
}
