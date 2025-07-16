package com.gitsunjaeab.mapick.domain.member;

import com.gitsunjaeab.mapick.domain.auth.LoginType;
import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditor;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.domain.report.Report;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "member")
    private Set<Roadmap> memberRoadmaps = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<RoadmapEditor> memberRoadmapEditors = new HashSet<>();

    @OneToMany(mappedBy = "invitedBy")
    private Set<RoadmapEditor> invitedByRoadmapEditors = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Layer> memberLayers = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Marker> memberMarkers = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Comment> memberComments = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Bookmark> memberLikes = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<MemberInterest> memberMemberInterests = new HashSet<>();

    @OneToMany(mappedBy = "reporter")
    private Set<Report> reporterReports = new HashSet<>();

    @OneToMany(mappedBy = "reportedMember")
    private Set<Report> reportedMemberReports = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Quest> memberQuests = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<MemberQuest> memberMemberQuests = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<QuestRank> memberQuestTopSubmitters = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<LayerLibrary> memberLayerLibraries = new HashSet<>();

}