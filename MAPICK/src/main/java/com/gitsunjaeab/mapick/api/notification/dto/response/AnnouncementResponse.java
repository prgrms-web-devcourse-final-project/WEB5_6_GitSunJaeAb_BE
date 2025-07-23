package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.notification.Announcement;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    private Long id;
    private String title;
    private String content;
    private MemberSimpleDTO member;
    private OffsetDateTime createdAt;


    // 생성 패턴용
    public static AnnouncementResponse of(Announcement announcement, String message) {
        return new AnnouncementResponse(
            ResponseCode.OK.getCode(),
            message,
            LocalDateTime.now(),
            announcement.getId(),
            announcement.getTitle(),
            announcement.getContent(),
            MemberSimpleDTO.of(announcement.getMember()),
            announcement.getCreatedAt()
        );
    }
}
