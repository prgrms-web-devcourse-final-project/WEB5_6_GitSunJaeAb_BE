package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestEvidenceRepository;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceDTO;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestEvidence;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MemberQuestEvidenceService {

    private final MemberQuestEvidenceRepository memberQuestEvidenceRepository;
    private final MemberQuestRepository memberQuestRepository;

    public MemberQuestEvidenceService(
            final MemberQuestEvidenceRepository memberQuestEvidenceRepository,
            final MemberQuestRepository memberQuestRepository) {
        this.memberQuestEvidenceRepository = memberQuestEvidenceRepository;
        this.memberQuestRepository = memberQuestRepository;
    }

    public List<MemberQuestEvidenceDTO> findAll() {
        final List<MemberQuestEvidence> memberQuestEvidences = memberQuestEvidenceRepository.findAll(Sort.by("id"));
        return memberQuestEvidences.stream()
                .map(memberQuestEvidence -> roadmapToDTO(memberQuestEvidence, new MemberQuestEvidenceDTO()))
                .toList();
    }

    public MemberQuestEvidenceDTO get(final Long id) {
        return memberQuestEvidenceRepository.findById(id)
                .map(memberQuestEvidence -> roadmapToDTO(memberQuestEvidence, new MemberQuestEvidenceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberQuestEvidenceDTO memberQuestEvidenceDTO) {
        final MemberQuestEvidence memberQuestEvidence = new MemberQuestEvidence();
        roadmapToEntity(memberQuestEvidenceDTO, memberQuestEvidence);
        return memberQuestEvidenceRepository.save(memberQuestEvidence).getId();
    }

    public void update(final Long id, final MemberQuestEvidenceDTO memberQuestEvidenceDTO) {
        final MemberQuestEvidence memberQuestEvidence = memberQuestEvidenceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(memberQuestEvidenceDTO, memberQuestEvidence);
        memberQuestEvidenceRepository.save(memberQuestEvidence);
    }

    public void delete(final Long id) {
        memberQuestEvidenceRepository.deleteById(id);
    }

    private MemberQuestEvidenceDTO roadmapToDTO(final MemberQuestEvidence memberQuestEvidence,
            final MemberQuestEvidenceDTO memberQuestEvidenceDTO) {
        memberQuestEvidenceDTO.setId(memberQuestEvidence.getId());
        memberQuestEvidenceDTO.setImageUrl(memberQuestEvidence.getImageUrl());
        memberQuestEvidenceDTO.setDescription(memberQuestEvidence.getDescription());
        memberQuestEvidenceDTO.setCreatedAt(memberQuestEvidence.getCreatedAt());
        memberQuestEvidenceDTO.setUpdatedAt(memberQuestEvidence.getUpdatedAt());
        memberQuestEvidenceDTO.setDeletedAt(memberQuestEvidence.getDeletedAt());
        memberQuestEvidenceDTO.setMemberQuest(memberQuestEvidence.getMemberQuest() == null ? null : memberQuestEvidence.getMemberQuest().getId());
        return memberQuestEvidenceDTO;
    }

    private MemberQuestEvidence roadmapToEntity(final MemberQuestEvidenceDTO memberQuestEvidenceDTO,
            final MemberQuestEvidence memberQuestEvidence) {
        memberQuestEvidence.setImageUrl(memberQuestEvidenceDTO.getImageUrl());
        memberQuestEvidence.setDescription(memberQuestEvidenceDTO.getDescription());
        memberQuestEvidence.setCreatedAt(memberQuestEvidenceDTO.getCreatedAt());
        memberQuestEvidence.setUpdatedAt(memberQuestEvidenceDTO.getUpdatedAt());
        memberQuestEvidence.setDeletedAt(memberQuestEvidenceDTO.getDeletedAt());
        final MemberQuest memberQuest = memberQuestEvidenceDTO.getMemberQuest() == null ? null : memberQuestRepository.findById(memberQuestEvidenceDTO.getMemberQuest())
                .orElseThrow(() -> new NotFoundException("memberQuest not found"));
        memberQuestEvidence.setMemberQuest(memberQuest);
        return memberQuestEvidence;
    }

}
