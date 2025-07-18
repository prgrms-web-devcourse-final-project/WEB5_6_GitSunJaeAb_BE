package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Bookmarks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "roadmap_id"})
) // 북마크 중복 방지
@Getter
@Setter
public class Bookmark {

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
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
