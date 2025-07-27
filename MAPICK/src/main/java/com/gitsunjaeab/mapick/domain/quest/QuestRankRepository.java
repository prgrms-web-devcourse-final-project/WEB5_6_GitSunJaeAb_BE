package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestRankRepository extends JpaRepository<QuestRank, Long> {

    // 특정 퀘스트의 첫 랭크 1개
    QuestRank findFirstByQuest(Quest quest);

    //특정 멤버의 첫 랭크 1개
    QuestRank findFirstByMember(Member member);

    // 퀘스트 + 멤버 기준으로 랭크 1개 조회 (점수 누적에 필요)
    Optional<QuestRank> findByQuestAndMember(Quest quest, Member member);

    // 특정 퀘스트의 전체 랭킹 리스트 (등수 순으로 정렬)
    List<QuestRank> findByQuestIdOrderByRank(Long questId);
}
