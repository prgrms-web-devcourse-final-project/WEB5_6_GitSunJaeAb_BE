package com.gitsunjaeab.mapick.application.domain.notification;

import com.gitsunjaeab.mapick.application.domain.notification.code.AnnouncementType;
import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.comment.Comment;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.application.domain.quest.Quest;
import com.gitsunjaeab.mapick.application.domain.roadmap.bookmark.Bookmark;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerForkHistory;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.infra.converter.OffsetDateTimeConverter;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Notifications")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Notification {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "primary_sequence",
        allocationSize = 1,
        initialValue = 100
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment; // 댓글 내용 (COMMENT Type 에서만 사용)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Include
    private Member member;


    @Builder.Default
    @Column(nullable = false)
    private boolean isRead = false; // 기본값 : 읽지 않음

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "announcement_type")
    private AnnouncementType announcementType; // 공지 타입

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "notification_type")
    private NotificationType notificationType; // 알림 타입

    @CreatedDate
    @Convert(converter = OffsetDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Convert(converter = OffsetDateTimeConverter.class)
    @Column
    private OffsetDateTime updatedAt;

    @Convert(converter = OffsetDateTimeConverter.class)
    @Column
    private OffsetDateTime deletedAt;

    @Convert(converter = OffsetDateTimeConverter.class)
    @Column
    private OffsetDateTime readAt;

    // 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layer_library_id")
    private LayerLibrary layerLibrary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layer_id")
    private Layer layer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_quest_id")
    private MemberQuest memberQuest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layerforkHistory_id")
    private LayerForkHistory layerForkHistory;


    // ===== UTIL =====
    // Enum 값 저장 검증 로직
    @PrePersist
    private void validateBeforeSave() {
        if (notificationType == NotificationType.ALL) {
            throw new CommonException(ResponseCode.SAVE_FAILED,
                "NotificationType.All 은 저장할 수 없습니다.");
        }
    }
    // 알림의 sender (발신자) 리턴
    public Member getSenderMember() {
        if (this.layerLibrary != null && this.layerLibrary.getMember() != null) {
            return this.layerLibrary.getMember();
        } else if (this.quest != null && this.quest.getMember() != null) {
            return this.quest.getMember();
        } else if (this.comment != null && this.comment.getMember() != null) {
            return this.comment.getMember();
        } else if (this.layerForkHistory != null && this.layerForkHistory.getMember() != null) {
            return this.layerForkHistory.getMember();
        } else if (this.announcement != null && this.announcement.getMember() != null) {
            return this.announcement.getMember();
        } else if (this.bookmark != null && this.bookmark.getMember() != null) {
            return this.bookmark.getMember();
        }
        return null;
    }

}
