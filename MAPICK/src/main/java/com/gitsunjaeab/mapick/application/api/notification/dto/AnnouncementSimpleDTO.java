package com.gitsunjaeab.mapick.application.api.notification.dto;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.notification.Announcement;
import com.gitsunjaeab.mapick.application.domain.notification.code.AnnouncementType;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementSimpleDTO {
    private Long id; // 공지 ID
    private String title; // 공지 제목
    private String content; // 공지 내용
    private AnnouncementType announcementType;
    private OffsetDateTime createdAt; // 생성일
    private MemberSimpleDTO member; // 작성자 정보

    public static AnnouncementSimpleDTO from(Announcement announcement) {
        return new AnnouncementSimpleDTO(
            announcement.getId(),
            announcement.getTitle(),
            announcement.getContent(),
            announcement.getAnnouncementType(),
            announcement.getCreatedAt(),
            MemberSimpleDTO.of(announcement.getMember())
        );
    }
}
