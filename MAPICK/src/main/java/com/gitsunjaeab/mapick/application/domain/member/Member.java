package com.gitsunjaeab.mapick.application.domain.member;

import com.gitsunjaeab.mapick.application.domain.achievement.MemberAchievement;
import com.gitsunjaeab.mapick.application.domain.auth.code.LoginType;
import com.gitsunjaeab.mapick.application.domain.auth.code.Role;
import com.gitsunjaeab.mapick.application.domain.notification.Notification;
import com.gitsunjaeab.mapick.application.domain.roadmap.bookmark.Bookmark;
import com.gitsunjaeab.mapick.application.domain.comment.Comment;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmapeditor.RoadmapEditor;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.application.domain.quest.Quest;
import com.gitsunjaeab.mapick.application.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.application.domain.report.Report;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Members")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Member {

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

    @Column(nullable = false)
    private Boolean isBlacklisted = false;

    @Column
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @Column
    private String provider;

    @Column(nullable = false)
    private String role;

    @Column
    private String status;

    @Column
    private String profileImage;

    @Column
    private OffsetDateTime lastLogin;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long loginCount = 0L;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    // 생성자에서 기본값 설정
    public Member() {
        this.createdAt = OffsetDateTime.now();
        this.isBlacklisted = false;
    }

    // 업데이트 시간 설정
    public void updateTimestamp() {
        this.updatedAt = OffsetDateTime.now();
    }

    // 역할(Role) 체크 메서드
    public boolean hasRole(Role role) {
        return this.role != null && this.role.equals(role.name());
    }

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<Roadmap> memberRoadmaps = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<RoadmapEditor> memberRoadmapEditors = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<Layer> memberLayers = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<Marker> memberMarkers = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<Comment> memberComments = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<Bookmark> memberLikes = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<MemberInterest> memberMemberInterests = new HashSet<>();

    @OneToMany(mappedBy = "reporter")
    @Builder.Default
    private Set<Report> reporterReports = new HashSet<>();

    @OneToMany(mappedBy = "reportedMember")
    @Builder.Default
    private Set<Report> reportedMemberReports = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<Quest> memberQuests = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<MemberQuest> memberMemberQuests = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<QuestRank> memberQuestTopSubmitters = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<LayerLibrary> memberLayerLibraries = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<MemberAchievement> memberAchievements = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE,
        orphanRemoval = true)
    @Builder.Default
    private Set<Notification> notifications = new HashSet<>();

}