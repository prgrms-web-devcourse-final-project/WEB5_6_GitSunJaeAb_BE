package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestRankRepository extends JpaRepository<QuestRank, Long> {

    QuestRank findFirstByQuest(Quest quest);

    QuestRank findFirstByMember(Member member);

}
