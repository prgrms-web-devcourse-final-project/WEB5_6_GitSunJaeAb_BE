package com.gitsunjaeab.mapick.api.notification.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.domain.notification.Announcement;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementSimpleDTO {
    private Long id; // 공지 ID
    private String title; // 공지 제목
    private String content; // 공지 내용
    private OffsetDateTime createdAt; // 생성일
    private MemberSimpleDTO member; // 작성자 정보

    public static AnnouncementSimpleDTO from(Announcement announcement) {
        return new AnnouncementSimpleDTO(
            announcement.getId(),
            announcement.getTitle(),
            announcement.getContent(),
            announcement.getCreatedAt(),
            MemberSimpleDTO.of(announcement.getMember())
        );
    }
}
