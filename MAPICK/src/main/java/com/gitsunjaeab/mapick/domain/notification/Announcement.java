package com.gitsunjaeab.mapick.domain.notification;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.infra.converter.OffsetDateTimeConverter;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "Announcements")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Announcement {

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
    @JoinColumn(name = "member_id")
    @ToString.Include
    private Member member;

    @Column(nullable = false)
    private boolean isRead = false; // 기본값 : 읽지 않음

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "announcement_type")
    private AnnouncementType announcementType;

    @CreatedDate
    @Convert(converter = OffsetDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Convert(converter = OffsetDateTimeConverter.class)
    @Column
    private OffsetDateTime updatedAt;
    @Column
    private OffsetDateTime deletedAt;
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
    @JoinColumn(name = "member_quest_id")
    private MemberQuest memberQuest;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

}
