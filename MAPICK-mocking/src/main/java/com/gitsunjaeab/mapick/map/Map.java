package com.gitsunjaeab.mapick.map;

import com.gitsunjaeab.mapick.bookmark.Bookmark;
import com.gitsunjaeab.mapick.comment.Comment;
import com.gitsunjaeab.mapick.layer.Layer;
import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelation;
import com.gitsunjaeab.mapick.map_editor.MapEditor;
import com.gitsunjaeab.mapick.map_hashtag_relation.entity.MapHashtagRelation;
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
@Table(name = "Maps")
@Getter
@Setter
public class Map {

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
    @JoinColumn(name = "original_map_id")
    private Map originalMap;

    @OneToMany(mappedBy = "originalMap")
    private Set<Map> originalMapMaps = new HashSet<>();

    @OneToMany(mappedBy = "map")
    private Set<MapEditor> mapMapEditors = new HashSet<>();

    @OneToMany(mappedBy = "map")
    private Set<Layer> mapLayers = new HashSet<>();

    @OneToMany(mappedBy = "map")
    private Set<Comment> mapComments = new HashSet<>();

    @OneToMany(mappedBy = "map")
    private Set<Bookmark> mapLikes = new HashSet<>();

    @OneToMany(mappedBy = "map")
    private Set<MapCategoryRelation> mapMapCategoryRelations = new HashSet<>();

    @OneToMany(mappedBy = "map")
    private Set<MapHashtagRelation> mapMapHashtags = new HashSet<>();

    @OneToMany(mappedBy = "map")
    private Set<Report> mapReports = new HashSet<>();

}
