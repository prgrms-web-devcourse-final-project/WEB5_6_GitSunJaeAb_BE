package com.gitsunjaeab.mapick.application.domain.quest;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.notification.Notification;
import jakarta.persistence.CascadeType;
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
        initialValue = 100
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )

    //확인
    private Long id;

    @Column(nullable = false)
    private Boolean status;

    //정답여부
    @Column(name = "is_recognized")
    private String isRecognized;

    //
    @Column
    private String title;

    //확인
    @Column(nullable = true)  // 참여 신청 시에는 답변이 없어도 됨
    private String answer;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime completedAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    @Column
    private OffsetDateTime submitAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "image_url", nullable = true)
//    @Column(nullable = true) // 테스트용 배포시 false로 변환
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;


    @Column(columnDefinition = "text")
    private String description;


    @OneToMany(mappedBy = "memberQuest", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();



}
