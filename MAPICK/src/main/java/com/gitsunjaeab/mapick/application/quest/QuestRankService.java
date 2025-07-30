package com.gitsunjaeab.mapick.application.quest;


import com.gitsunjaeab.mapick.api.quest.dto.QuestRankResponse;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.domain.quest.QuestRankRepository;
import com.gitsunjaeab.mapick.infra.common.EntityFinder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestRankService {

    private final QuestRankRepository questRankRepository;
    private final EntityFinder entityFinder;


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
    public QuestRankResponse get(final Long questRankId) {
        QuestRank questRank = entityFinder.findByQuestRankId(questRankId);
        return toResponse(questRank);
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
