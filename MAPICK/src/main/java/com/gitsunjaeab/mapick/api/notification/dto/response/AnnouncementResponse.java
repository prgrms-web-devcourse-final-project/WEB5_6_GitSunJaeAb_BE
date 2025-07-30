package com.gitsunjaeab.mapick.api.notification.dto.response;

import com.gitsunjaeab.mapick.api.notification.dto.AnnouncementSimpleDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.notification.Announcement;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<AnnouncementSimpleDTO> announcements;

    // 단일 공지도 배열로 감싸서 반환
    public static AnnouncementResponse of(Announcement announcement, String message) {
        return new AnnouncementResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            List.of(AnnouncementSimpleDTO.from(announcement))
        );
    }
}
