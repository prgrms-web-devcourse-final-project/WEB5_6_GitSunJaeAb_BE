package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestDTO;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestEvidence;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestEvidenceRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final MemberRepository memberRepository;
    private final QuestRepository questRepository;
    private final MemberQuestEvidenceRepository memberQuestEvidenceRepository;

    public MemberQuestService(final MemberQuestRepository memberQuestRepository,
            final MemberRepository memberRepository, final QuestRepository questRepository,
            final MemberQuestEvidenceRepository memberQuestEvidenceRepository) {
        this.memberQuestRepository = memberQuestRepository;
        this.memberRepository = memberRepository;
        this.questRepository = questRepository;
        this.memberQuestEvidenceRepository = memberQuestEvidenceRepository;
    }

    public List<MemberQuestDTO> findAll() {
        final List<MemberQuest> memberQuests = memberQuestRepository.findAll(Sort.by("id"));
        return memberQuests.stream()
                .map(memberQuest -> roadmapToDTO(memberQuest, new MemberQuestDTO()))
                .toList();
    }

    public MemberQuestDTO get(final Long id) {
        return memberQuestRepository.findById(id)
                .map(memberQuest -> roadmapToDTO(memberQuest, new MemberQuestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberQuestDTO memberQuestDTO) {
        final MemberQuest memberQuest = new MemberQuest();
        roadmapToEntity(memberQuestDTO, memberQuest);
        return memberQuestRepository.save(memberQuest).getId();
    }

    public void update(final Long id, final MemberQuestDTO memberQuestDTO) {
        final MemberQuest memberQuest = memberQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(memberQuestDTO, memberQuest);
        memberQuestRepository.save(memberQuest);
    }

    public void delete(final Long id) {
        memberQuestRepository.deleteById(id);
    }

    private MemberQuestDTO roadmapToDTO(final MemberQuest memberQuest,
            final MemberQuestDTO memberQuestDTO) {
        memberQuestDTO.setId(memberQuest.getId());
        memberQuestDTO.setStatus(memberQuest.getStatus());
        memberQuestDTO.setAnswer(memberQuest.getAnswer());
        memberQuestDTO.setIsRecognized(memberQuest.getIsRecognized());
        memberQuestDTO.setCreatedAt(memberQuest.getCreatedAt());
        memberQuestDTO.setCompletedAt(memberQuest.getCompletedAt());
        memberQuestDTO.setUpdatedAt(memberQuest.getUpdatedAt());
        memberQuestDTO.setDeletedAt(memberQuest.getDeletedAt());
        memberQuestDTO.setMember(memberQuest.getMember() == null ? null : memberQuest.getMember().getId());
        memberQuestDTO.setQuest(memberQuest.getQuest() == null ? null : memberQuest.getQuest().getId());
        return memberQuestDTO;
    }

    private MemberQuest roadmapToEntity(final MemberQuestDTO memberQuestDTO,
            final MemberQuest memberQuest) {
        memberQuest.setStatus(memberQuestDTO.getStatus());
        memberQuest.setAnswer(memberQuestDTO.getAnswer());
        memberQuest.setIsRecognized(memberQuestDTO.getIsRecognized());
        memberQuest.setCreatedAt(memberQuestDTO.getCreatedAt());
        memberQuest.setCompletedAt(memberQuestDTO.getCompletedAt());
        memberQuest.setUpdatedAt(memberQuestDTO.getUpdatedAt());
        memberQuest.setDeletedAt(memberQuestDTO.getDeletedAt());
        final Member member = memberQuestDTO.getMember() == null ? null : memberRepository.findById(memberQuestDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberQuest.setMember(member);
        final Quest quest = memberQuestDTO.getQuest() == null ? null : questRepository.findById(memberQuestDTO.getQuest())
                .orElseThrow(() -> new NotFoundException("quest not found"));
        memberQuest.setQuest(quest);
        return memberQuest;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final MemberQuest memberQuest = memberQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final MemberQuestEvidence memberQuestMemberQuestEvidence = memberQuestEvidenceRepository.findFirstByMemberQuest(memberQuest);
        if (memberQuestMemberQuestEvidence != null) {
            referencedWarning.setKey("memberQuest.memberQuestEvidence.memberQuest.referenced");
            referencedWarning.addParam(memberQuestMemberQuestEvidence.getId());
            return referencedWarning;
        }
        return null;
    }

}
