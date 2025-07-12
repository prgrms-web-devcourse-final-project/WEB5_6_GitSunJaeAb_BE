package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberQuestRepository extends JpaRepository<MemberQuest, Long> {

    MemberQuest findFirstByQuest(Quest quest);

    MemberQuest findFirstByMember(Member member);

    // 특정 퀘스트의 참여자 목록 조회
    List<MemberQuest> findByQuestId(Long questId);
}
