package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import feign.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface MemberQuestRepository extends JpaRepository<MemberQuest, Long> {

    MemberQuest findFirstByQuest(Quest quest);

    MemberQuest findFirstByMember(Member member);

    // 특정 퀘스트의 참여자 목록 조회
//    List<MemberQuest> findByQuestId(Long questId);

    // LAZY 문제 해결용 쿼리 추가
    @Query("SELECT mq FROM MemberQuest mq " +
        "JOIN FETCH mq.quest q " +
        "JOIN FETCH q.member " +
        "WHERE mq.id = :id")
    Optional<MemberQuest> findWithQuestAndMemberById(@Param("id") Long id);


    @Query("SELECT mq FROM MemberQuest mq JOIN FETCH mq.member WHERE mq.quest.id = :questId")
    List<MemberQuest> findWithMemberByQuestId(@Param("questId") Long questId);
}
