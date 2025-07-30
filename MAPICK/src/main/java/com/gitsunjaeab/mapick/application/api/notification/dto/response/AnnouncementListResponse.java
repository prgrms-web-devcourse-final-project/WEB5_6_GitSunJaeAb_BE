package com.gitsunjaeab.mapick.application.api.notification.dto.response;

import com.gitsunjaeab.mapick.application.api.notification.dto.AnnouncementSimpleDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.notification.Announcement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<AnnouncementSimpleDTO> announcements;

    public static AnnouncementListResponse of(List<Announcement> entities, String message) {
        List<AnnouncementSimpleDTO> dtos = entities.stream()
            .map(AnnouncementSimpleDTO::from)
            .collect(Collectors.toList());
        return new AnnouncementListResponse(
            ResponseCode.OK.getCode(),
            message,
            OffsetDateTime.now(),
            dtos
        );
    }
} 