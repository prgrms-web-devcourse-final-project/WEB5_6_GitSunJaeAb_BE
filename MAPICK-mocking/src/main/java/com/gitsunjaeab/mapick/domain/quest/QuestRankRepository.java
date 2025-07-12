package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestRankRepository extends JpaRepository<QuestRank, Long> {

    QuestRank findFirstByQuest(Quest quest);

    QuestRank findFirstByMember(Member member);

    // 특정 퀘스트의 랭킹을 순위 순으로 조회
    List<QuestRank> findByQuestIdOrderByRank(Long questId);
}
