package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.report.Report;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
            initialValue = 10000
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
    private String roadmapType;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_roadmap_id")
    private Roadmap originalRoadmap;

    @OneToMany(mappedBy = "originalRoadmap")
    private Set<Roadmap> originalRoadmaps = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<RoadmapEditor> roadmapEditors = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Layer> roadmapLayers = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Comment> roadmapComments = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Bookmark> roadmapLikes = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<RoadmapHashtagRelation> roadmapMapHashtags = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Report> roadmapReports = new HashSet<>();

}
