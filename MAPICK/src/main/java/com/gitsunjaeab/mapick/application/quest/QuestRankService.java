package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRankRepository;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRankResponse;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class QuestRankService {

    private final QuestRankRepository questRankRepository;

//    // 점수 추가 (랭킹 없으면 생성, 있으면 누적)
//    @Transactional
//    public void addScore(Member member, Quest quest, int score) {
//        QuestRank rank = questRankRepository.findByQuestAndMember(quest, member)
//            .orElseGet(() -> {
//                QuestRank newRank = new QuestRank();
//                newRank.setQuest(quest);
//                newRank.setMember(member);
//                newRank.setScore(0);
//                newRank.setRank(0);
//                newRank.setCreatedAt(OffsetDateTime.now());
//                return newRank;
//            });
//
//        rank.setScore(rank.getScore() + score);
//        questRankRepository.save(rank);
//    }

    // 전체 랭킹 조회
    public List<QuestRankResponse> findAll() {
        final List<QuestRank> questRanks = questRankRepository.findAll(Sort.by("rank"));
        return questRanks.stream()
            .map(this::toResponse)
            .toList();
    }

    // 특정 퀘스트 랭킹 조회
    public List<QuestRankResponse> findByQuestId(final Long questId) {
        final List<QuestRank> questRanks = questRankRepository.findByQuestIdOrderByRank(questId);
        return questRanks.stream()
            .map(this::toResponse)
            .toList();
    }

    // 단일 랭킹 조회
    public QuestRankResponse get(final Long id) {
        return questRankRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(NotFoundException::new);
    }

    // 랭킹 삭제
    public void delete(final Long id) {
        questRankRepository.deleteById(id);
    }

    // Entity → Response DTO 변환
    private QuestRankResponse toResponse(final QuestRank questRank) {
        return QuestRankResponse.ofCreate(questRank);
    }
}
