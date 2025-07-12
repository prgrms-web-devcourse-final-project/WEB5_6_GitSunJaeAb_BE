package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.domain.quest.QuestRankRepository;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRankResponse;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class QuestRankService {

    private final QuestRankRepository questRankRepository;

    public QuestRankService(final QuestRankRepository questRankRepository) {
        this.questRankRepository = questRankRepository;
    }

    // 전체 랭킹 조회
    public List<QuestRankResponse> findAll() {
        final List<QuestRank> questRanks = questRankRepository.findAll(Sort.by("rank"));
        return questRanks.stream()
                .map(this::toResponse)
                .toList();
    }

    // 특정 퀘스트의 랭킹 조회
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

    // Entity → Response 변환
    private QuestRankResponse toResponse(final QuestRank questRank) {
        QuestRankResponse response = new QuestRankResponse();
        response.setId(questRank.getId());
        response.setRank(questRank.getRank());
        response.setScore(null); // QuestRank 엔티티에 score 필드가 없음
        response.setCreatedAt(questRank.getCreatedAt());
        response.setUpdatedAt(questRank.getUpdatedAt());
        response.setQuest(questRank.getQuest() == null ? null : questRank.getQuest().getId());
        response.setMember(questRank.getMember() == null ? null : questRank.getMember().getId());
        return response;
    }
}
