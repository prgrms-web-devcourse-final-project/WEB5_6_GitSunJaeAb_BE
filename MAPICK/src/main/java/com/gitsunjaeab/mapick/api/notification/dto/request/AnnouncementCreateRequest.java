package com.gitsunjaeab.mapick.api.notification.dto.request;

import com.gitsunjaeab.mapick.domain.notification.AnnouncementType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnnouncementCreateRequest {
    // 공지 제목
    private String title;
    // 공지 내용
    private String content;
    // 공지 타입 (예: SYSTEM, EVENT)
    private AnnouncementType announcementType;
}
