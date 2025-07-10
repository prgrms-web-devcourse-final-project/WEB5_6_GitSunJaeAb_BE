package com.gitsunjaeab.mapick.roadmap_editor;

import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap_editor.dto.RoadmapEditorDTO;
import com.gitsunjaeab.mapick.roadmap_editor.entity.RoadmapEditor;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


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
