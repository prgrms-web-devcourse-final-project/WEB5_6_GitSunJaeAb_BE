package com.gitsunjaeab.mapick.roadmap.entity;

import com.gitsunjaeab.mapick.bookmark.entity.Bookmark;
import com.gitsunjaeab.mapick.comment.entity.Comment;
import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.roadmap_category_relation.entity.RoadmapCategoryRelation;
import com.gitsunjaeab.mapick.roadmap_editor.entity.RoadmapEditor;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.entity.RoadmapHashtagRelation;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.report.entity.Report;
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
    private String mapType;

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
    private Set<Roadmap> originalMapRoadmaps = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<RoadmapEditor> mapRoadmapEditors = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Layer> mapLayers = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Comment> mapComments = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Bookmark> mapLikes = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<RoadmapCategoryRelation> mapMapCategoryRelations = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<RoadmapHashtagRelation> mapMapHashtags = new HashSet<>();

    @OneToMany(mappedBy = "roadmap")
    private Set<Report> mapReports = new HashSet<>();

}
