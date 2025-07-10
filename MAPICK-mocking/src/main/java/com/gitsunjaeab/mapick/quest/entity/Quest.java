package com.gitsunjaeab.mapick.quest.entity;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member_quest.entity.MemberQuest;
import com.gitsunjaeab.mapick.quest_rank.entity.QuestRank;
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
@Table(name = "Quests")
@Getter
@Setter
public class Quest {

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

    @Column(nullable = false)
    private String questImage;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Boolean isActive;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime completedAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    @OneToMany(mappedBy = "quest")
    private Set<Report> questReports = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "quest")
    private Set<MemberQuest> questMemberQuests = new HashSet<>();

    @OneToMany(mappedBy = "quest")
    private Set<QuestRank> questQuestTopSubmitters = new HashSet<>();

}
