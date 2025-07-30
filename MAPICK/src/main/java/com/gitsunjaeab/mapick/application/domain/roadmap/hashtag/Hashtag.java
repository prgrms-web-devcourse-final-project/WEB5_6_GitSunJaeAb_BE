package com.gitsunjaeab.mapick.application.domain.roadmap.hashtag;

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
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Hashtags")
@Getter
@Setter
@NoArgsConstructor
public class Hashtag {

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

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "hashtag")
    private Set<RoadmapHashtagRelation> roadmapHashtags = new HashSet<>();

    public Hashtag(String name) {
        this.name = name;
        this.createdAt = OffsetDateTime.now();
    }

}
