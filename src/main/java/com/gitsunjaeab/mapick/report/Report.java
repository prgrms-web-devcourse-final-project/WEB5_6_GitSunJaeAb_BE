package com.gitsunjaeab.mapick.report;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.marker.Marker;
import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.quest.Quest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Reports")
@Getter
@Setter
public class Report {

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
    private String reportType;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String status;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id")
    private Map map;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marker_id")
    private Marker marker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;

}
