package com.gitsunjaeab.mapick.member.entity;

import com.gitsunjaeab.mapick.bookmark.entity.Bookmark;
import com.gitsunjaeab.mapick.comment.entity.Comment;
import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.layer_library.entity.LayerLibrary;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap_editor.entity.RoadmapEditor;
import com.gitsunjaeab.mapick.marker.entity.Marker;
import com.gitsunjaeab.mapick.member_interest.entity.MemberInterest;
import com.gitsunjaeab.mapick.member_quest.entity.MemberQuest;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import com.gitsunjaeab.mapick.quest_rank.entity.QuestRank;
import com.gitsunjaeab.mapick.report.entity.Report;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Members")
@Getter
@Setter
public class Member {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "primary_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String loginType;

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