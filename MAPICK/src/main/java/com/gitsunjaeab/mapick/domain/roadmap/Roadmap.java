package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.notification.Notification;
import com.gitsunjaeab.mapick.domain.report.Report;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Roadmaps")
@Getter
@Setter
public class Roadmap {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String thumbnail;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean isAnimated;

    @Column
    private Integer likeCount;

    @Column
    private Integer viewCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoadmapType roadmapType;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    @Column(length = 255)
    private String address;

    @Column
    private Double regionLatitude;

    @Column
    private Double regionLongitude;

    @Column
    private OffsetDateTime participationEnd;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<RoadmapEditor> roadmapEditors = new HashSet<>();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Layer> roadmapLayers = new HashSet<>();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Comment> roadmapComments = new HashSet<>();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Bookmark> roadmapLikes = new HashSet<>();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<RoadmapHashtagRelation> roadmapMapHashtags = new HashSet<>();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Report> roadmapReports = new HashSet<>();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();
}
