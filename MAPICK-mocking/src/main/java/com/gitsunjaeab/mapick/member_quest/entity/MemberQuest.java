package com.gitsunjaeab.mapick.member_quest.entity;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member_quest_evidence.entity.MemberQuestEvidence;
import com.gitsunjaeab.mapick.quest.entity.Quest;
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
@Table(name = "MemberQuests")
@Getter
@Setter
public class MemberQuest {

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
    private String status;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private String isRecognized;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime completedAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @OneToMany(mappedBy = "memberQuest")
    private Set<MemberQuestEvidence> memberQuestMemberQuestEvidences = new HashSet<>();

}
