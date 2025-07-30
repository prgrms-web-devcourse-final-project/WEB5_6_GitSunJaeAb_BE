package com.gitsunjaeab.mapick.application.api.notification.dto.internal;

import com.gitsunjaeab.mapick.application.api.comment.dto.internal.CommentSimpleDTO;
import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.application.api.quest.dto.internal.QuestSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.notification.code.AnnouncementType;
import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCommentsListDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String content;

    private CommentSimpleDTO comment; // 댓글 정보(내용, 작성자 등)
    // 수신인(알림 받는 사람)
    private MemberSimpleDTO receiver;
    // 연관 로드맵/퀘스트 정보(간단 DTO)
    private RoadmapSimpleDTO roadmap; // 로드맵 정보
    private QuestSimpleDTO quest;     // 퀘스트 정보

    private boolean isRead;

    private AnnouncementType announcementType;

    private NotificationType notificationType;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private OffsetDateTime readAt;

}
