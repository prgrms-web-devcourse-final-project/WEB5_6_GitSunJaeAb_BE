package com.gitsunjaeab.mapick.quest_rank;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import com.gitsunjaeab.mapick.quest_rank.entity.QuestRank;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestRankRepository extends JpaRepository<QuestRank, Long> {

    QuestRank findFirstByQuest(Quest quest);

    QuestRank findFirstByMember(Member member);

}
