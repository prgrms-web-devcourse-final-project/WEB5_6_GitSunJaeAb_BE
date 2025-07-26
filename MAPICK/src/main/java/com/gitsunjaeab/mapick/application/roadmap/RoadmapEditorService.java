package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapEditorSimpleDTO;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditorRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapEditorDTO;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditor;
import com.gitsunjaeab.mapick.util.NotFoundException;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 공유지도
@Service
public class RoadmapEditorService {

    private final RoadmapEditorRepository roadmapEditorRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public RoadmapEditorService(final RoadmapEditorRepository roadmapEditorRepository,
            final RoadmapRepository roadmapRepository, final MemberRepository memberRepository) {
        this.roadmapEditorRepository = roadmapEditorRepository;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
    }

    public List<RoadmapEditorDTO> findAll() {
        final List<RoadmapEditor> roadmapEditors = roadmapEditorRepository.findAll(Sort.by("id"));
        return roadmapEditors.stream()
                .map(roadmapEditor -> roadmapToDTO(roadmapEditor, new RoadmapEditorDTO()))
                .toList();
    }

    public RoadmapEditorDTO get(final Long id) {
        return roadmapEditorRepository.findById(id)
                .map(roadmapEditor -> roadmapToDTO(roadmapEditor, new RoadmapEditorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoadmapEditorDTO roadmapEditorDTO) {
        final RoadmapEditor roadmapEditor = new RoadmapEditor();
        roadmapToEntity(roadmapEditorDTO, roadmapEditor);
        return roadmapEditorRepository.save(roadmapEditor).getId();
    }

    @Transactional(readOnly = true)
    public List<RoadmapEditorSimpleDTO> getRoadmapEditors(Long roadmapId) {
        return roadmapEditorRepository.findAllEditorsByRoadmapId(roadmapId);
    }

    @Transactional(readOnly = true)
    public long countRoadmapEditors(Long roadmapId) {
        return roadmapEditorRepository.countByRoadmapIdAndDeletedAtIsNull(roadmapId);
    }

    @Transactional
    public void registerEditorIfNotExists(Long roadmapId, Long memberId) {
        boolean exists = roadmapEditorRepository.existsByRoadmapIdAndMemberId(roadmapId, memberId);
        if (!exists) {
            Roadmap roadmap = roadmapRepository.findById(roadmapId)
                    .orElseThrow(() -> new NotFoundException("로드맵이 존재하지 않습니다."));
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException("사용자가 존재하지 않습니다."));

            RoadmapEditor editor = new RoadmapEditor();
            editor.setRoadmap(roadmap);
            editor.setMember(member);
            editor.setPermission("EDIT"); // 기본 권한
            editor.setCreatedAt(OffsetDateTime.now());

            roadmapEditorRepository.save(editor);
        }
    }

    public void update(final Long id, final RoadmapEditorDTO roadmapEditorDTO) {
        final RoadmapEditor roadmapEditor = roadmapEditorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(roadmapEditorDTO, roadmapEditor);
        roadmapEditorRepository.save(roadmapEditor);
    }

    public void delete(final Long id) {
        roadmapEditorRepository.deleteById(id);
    }

    private RoadmapEditorDTO roadmapToDTO(final RoadmapEditor roadmapEditor, final RoadmapEditorDTO roadmapEditorDTO) {
        roadmapEditorDTO.setId(roadmapEditor.getId());
        roadmapEditorDTO.setPermission(roadmapEditor.getPermission());
        roadmapEditorDTO.setCreatedAt(roadmapEditor.getCreatedAt());
        roadmapEditorDTO.setUpdatedAt(roadmapEditor.getUpdatedAt());
        roadmapEditorDTO.setDeletedAt(roadmapEditor.getDeletedAt());
        roadmapEditorDTO.setRoadmap(roadmapEditor.getRoadmap() == null ? null : roadmapEditor.getRoadmap().getId());
        roadmapEditorDTO.setMember(roadmapEditor.getMember() == null ? null : roadmapEditor.getMember().getId());
        roadmapEditorDTO.setInvitedBy(
            roadmapEditor.getInvitedBy() == null ? null : roadmapEditor.getInvitedBy().getId());
        return roadmapEditorDTO;
    }

    private RoadmapEditor roadmapToEntity(final RoadmapEditorDTO roadmapEditorDTO, final RoadmapEditor roadmapEditor) {
        roadmapEditor.setPermission(roadmapEditorDTO.getPermission());
        roadmapEditor.setCreatedAt(roadmapEditorDTO.getCreatedAt());
        roadmapEditor.setUpdatedAt(roadmapEditorDTO.getUpdatedAt());
        roadmapEditor.setDeletedAt(roadmapEditorDTO.getDeletedAt());
        final Roadmap roadmap = roadmapEditorDTO.getRoadmap() == null ? null : roadmapRepository.findById(roadmapEditorDTO.getRoadmap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        roadmapEditor.setRoadmap(roadmap);
        final Member member = roadmapEditorDTO.getMember() == null ? null : memberRepository.findById(roadmapEditorDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        roadmapEditor.setMember(member);
        final Member invitedBy = roadmapEditorDTO.getInvitedBy() == null ? null : memberRepository.findById(roadmapEditorDTO.getInvitedBy())
                .orElseThrow(() -> new NotFoundException("invitedBy not found"));
        roadmapEditor.setInvitedBy(invitedBy);
        return roadmapEditor;
    }

}
