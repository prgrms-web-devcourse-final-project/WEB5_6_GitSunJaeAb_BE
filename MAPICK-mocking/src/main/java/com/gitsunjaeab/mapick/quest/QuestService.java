package com.gitsunjaeab.mapick.quest;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.member_quest.entity.MemberQuest;
import com.gitsunjaeab.mapick.member_quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import com.gitsunjaeab.mapick.quest_rank.entity.QuestRank;
import com.gitsunjaeab.mapick.quest_rank.QuestRankRepository;
import com.gitsunjaeab.mapick.report.entity.Report;
import com.gitsunjaeab.mapick.report.ReportRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final QuestRankRepository questRankRepository;

    public QuestService(final QuestRepository questRepository,
            final MemberRepository memberRepository, final ReportRepository reportRepository,
            final MemberQuestRepository memberQuestRepository,
            final QuestRankRepository questRankRepository) {
        this.questRepository = questRepository;
        this.memberRepository = memberRepository;
        this.reportRepository = reportRepository;
        this.memberQuestRepository = memberQuestRepository;
        this.questRankRepository = questRankRepository;
    }

    public List<QuestDTO> findAll() {
        final List<Quest> quests = questRepository.findAll(Sort.by("id"));
        return quests.stream()
                .map(quest -> mapToDTO(quest, new QuestDTO()))
                .toList();
    }

    public QuestDTO get(final Long id) {
        return questRepository.findById(id)
                .map(quest -> mapToDTO(quest, new QuestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final QuestDTO questDTO) {
        final Quest quest = new Quest();
        mapToEntity(questDTO, quest);
        return questRepository.save(quest).getId();
    }

    public void update(final Long id, final QuestDTO questDTO) {
        final Quest quest = questRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(questDTO, quest);
        questRepository.save(quest);
    }

    public void delete(final Long id) {
        questRepository.deleteById(id);
    }

    private QuestDTO mapToDTO(final Quest quest, final QuestDTO questDTO) {
        questDTO.setId(quest.getId());
        questDTO.setTitle(quest.getTitle());
        questDTO.setQuestImage(quest.getQuestImage());
        questDTO.setDescription(quest.getDescription());
        questDTO.setIsActive(quest.getIsActive());
        questDTO.setCreatedAt(quest.getCreatedAt());
        questDTO.setCompletedAt(quest.getCompletedAt());
        questDTO.setUpdatedAt(quest.getUpdatedAt());
        questDTO.setDeletedAt(quest.getDeletedAt());
        questDTO.setMember(quest.getMember() == null ? null : quest.getMember().getId());
        return questDTO;
    }

    private Quest mapToEntity(final QuestDTO questDTO, final Quest quest) {
        quest.setTitle(questDTO.getTitle());
        quest.setQuestImage(questDTO.getQuestImage());
        quest.setDescription(questDTO.getDescription());
        quest.setIsActive(questDTO.getIsActive());
        quest.setCreatedAt(questDTO.getCreatedAt());
        quest.setCompletedAt(questDTO.getCompletedAt());
        quest.setUpdatedAt(questDTO.getUpdatedAt());
        quest.setDeletedAt(questDTO.getDeletedAt());
        final Member member = questDTO.getMember() == null ? null : memberRepository.findById(questDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        quest.setMember(member);
        return quest;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Quest quest = questRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Report questReport = reportRepository.findFirstByQuest(quest);
        if (questReport != null) {
            referencedWarning.setKey("quest.report.quest.referenced");
            referencedWarning.addParam(questReport.getId());
            return referencedWarning;
        }
        final MemberQuest questMemberQuest = memberQuestRepository.findFirstByQuest(quest);
        if (questMemberQuest != null) {
            referencedWarning.setKey("quest.memberQuest.quest.referenced");
            referencedWarning.addParam(questMemberQuest.getId());
            return referencedWarning;
        }
        final QuestRank questQuestRank = questRankRepository.findFirstByQuest(quest);
        if (questQuestRank != null) {
            referencedWarning.setKey("quest.questRank.quest.referenced");
            referencedWarning.addParam(questQuestRank.getId());
            return referencedWarning;
        }
        return null;
    }

}
