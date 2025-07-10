package com.gitsunjaeab.mapick.quest_rank;

import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.quest.Quest;
import com.gitsunjaeab.mapick.quest.QuestRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class QuestRankService {

    private final QuestRankRepository questRankRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;

    public QuestRankService(final QuestRankRepository questRankRepository,
            final QuestRepository questRepository, final MemberRepository memberRepository) {
        this.questRankRepository = questRankRepository;
        this.questRepository = questRepository;
        this.memberRepository = memberRepository;
    }

    public List<QuestRankDTO> findAll() {
        final List<QuestRank> questRanks = questRankRepository.findAll(Sort.by("id"));
        return questRanks.stream()
                .map(questRank -> mapToDTO(questRank, new QuestRankDTO()))
                .toList();
    }

    public QuestRankDTO get(final Long id) {
        return questRankRepository.findById(id)
                .map(questRank -> mapToDTO(questRank, new QuestRankDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final QuestRankDTO questRankDTO) {
        final QuestRank questRank = new QuestRank();
        mapToEntity(questRankDTO, questRank);
        return questRankRepository.save(questRank).getId();
    }

    public void update(final Long id, final QuestRankDTO questRankDTO) {
        final QuestRank questRank = questRankRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(questRankDTO, questRank);
        questRankRepository.save(questRank);
    }

    public void delete(final Long id) {
        questRankRepository.deleteById(id);
    }

    private QuestRankDTO mapToDTO(final QuestRank questRank, final QuestRankDTO questRankDTO) {
        questRankDTO.setId(questRank.getId());
        questRankDTO.setRank(questRank.getRank());
        questRankDTO.setCompletedAt(questRank.getCompletedAt());
        questRankDTO.setCreatedAt(questRank.getCreatedAt());
        questRankDTO.setUpdatedAt(questRank.getUpdatedAt());
        questRankDTO.setDeletedAt(questRank.getDeletedAt());
        questRankDTO.setQuest(questRank.getQuest() == null ? null : questRank.getQuest().getId());
        questRankDTO.setMember(questRank.getMember() == null ? null : questRank.getMember().getId());
        return questRankDTO;
    }

    private QuestRank mapToEntity(final QuestRankDTO questRankDTO, final QuestRank questRank) {
        questRank.setRank(questRankDTO.getRank());
        questRank.setCompletedAt(questRankDTO.getCompletedAt());
        questRank.setCreatedAt(questRankDTO.getCreatedAt());
        questRank.setUpdatedAt(questRankDTO.getUpdatedAt());
        questRank.setDeletedAt(questRankDTO.getDeletedAt());
        final Quest quest = questRankDTO.getQuest() == null ? null : questRepository.findById(questRankDTO.getQuest())
                .orElseThrow(() -> new NotFoundException("quest not found"));
        questRank.setQuest(quest);
        final Member member = questRankDTO.getMember() == null ? null : memberRepository.findById(questRankDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        questRank.setMember(member);
        return questRank;
    }

}
