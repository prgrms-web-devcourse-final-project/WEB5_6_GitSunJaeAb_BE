package com.gitsunjaeab.mapick.category.entity;

import com.gitsunjaeab.mapick.roadmap_category_relation.entity.RoadmapCategoryRelation;
import com.gitsunjaeab.mapick.member_interest.entity.MemberInterest;
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
@Table(name = "Categories")
@Getter
@Setter
public class Category {

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
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String categoryImage;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "interest")
    private Set<MemberInterest> interestMemberInterests = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<RoadmapCategoryRelation> roadmapCategoryRelations = new HashSet<>();

}
