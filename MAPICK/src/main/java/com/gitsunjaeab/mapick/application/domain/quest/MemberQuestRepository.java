package com.gitsunjaeab.mapick.application.domain.quest;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import feign.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface MemberQuestRepository extends JpaRepository<MemberQuest, Long> {

    MemberQuest findFirstByQuest(Quest quest);

    MemberQuest findFirstByMember(Member member);

    @Query("SELECT mq FROM MemberQuest mq JOIN FETCH mq.member WHERE mq.member = :member")
    List<MemberQuest> findByMember(Member member);

    @Query("SELECT mq FROM MemberQuest mq JOIN FETCH mq.member JOIN FETCH mq.quest q WHERE mq.member = :member AND q.deletedAt IS NULL")
    List<MemberQuest> findByMemberAndActiveQuest(@Param("member") Member member);

    @Query("SELECT mq FROM MemberQuest mq JOIN FETCH mq.member WHERE mq.quest.id = :questId AND mq.isRecognized = true")
    List<MemberQuest> findByQuestIdAndRecognizedTrue(@Param("questId") Long questId);


    @Query("SELECT mq FROM MemberQuest mq JOIN FETCH mq.quest q JOIN FETCH q.member WHERE mq.id = :id")
    Optional<MemberQuest> findWithQuestAndMemberById(@Param("id") Long id);

    //mypage 확인용
    @Query("SELECT mq FROM MemberQuest mq JOIN FETCH mq.member WHERE mq.quest.id = :questId")
    List<MemberQuest> findWithMemberByQuestId(@Param("questId") Long questId);


}
