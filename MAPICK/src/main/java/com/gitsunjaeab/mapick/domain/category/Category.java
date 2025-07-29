package com.gitsunjaeab.mapick.domain.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import jakarta.persistence.CascadeType;
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
@Table(name = "Categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

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
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String categoryImage;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<MemberInterest> interestMemberInterests = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Roadmap> roadmaps = new HashSet<>();

    public Category(CategoryRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.createdAt = OffsetDateTime.now();
    }
}
