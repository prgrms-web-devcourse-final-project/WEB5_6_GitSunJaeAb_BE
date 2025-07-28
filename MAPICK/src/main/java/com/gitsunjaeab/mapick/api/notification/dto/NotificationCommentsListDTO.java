package com.gitsunjaeab.mapick.api.notification.dto;

import com.gitsunjaeab.mapick.api.comment.dto.CommentSimpleDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestSimpleDTO;
import com.gitsunjaeab.mapick.domain.notification.AnnouncementType;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
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
