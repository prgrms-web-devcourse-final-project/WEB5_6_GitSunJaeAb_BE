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
                .map(roadmapEditor -> mapToDTO(roadmapEditor, new RoadmapEditorDTO()))
                .toList();
    }

    public RoadmapEditorDTO get(final Long id) {
        return roadmapEditorRepository.findById(id)
                .map(roadmapEditor -> mapToDTO(roadmapEditor, new RoadmapEditorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoadmapEditorDTO roadmapEditorDTO) {
        final RoadmapEditor roadmapEditor = new RoadmapEditor();
        mapToEntity(roadmapEditorDTO, roadmapEditor);
        return roadmapEditorRepository.save(roadmapEditor).getId();
    }

    public void update(final Long id, final RoadmapEditorDTO roadmapEditorDTO) {
        final RoadmapEditor roadmapEditor = roadmapEditorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roadmapEditorDTO, roadmapEditor);
        roadmapEditorRepository.save(roadmapEditor);
    }

    public void delete(final Long id) {
        roadmapEditorRepository.deleteById(id);
    }

    private RoadmapEditorDTO mapToDTO(final RoadmapEditor roadmapEditor, final RoadmapEditorDTO mapEditorDTO) {
        mapEditorDTO.setId(roadmapEditor.getId());
        mapEditorDTO.setPermission(roadmapEditor.getPermission());
        mapEditorDTO.setCreatedAt(roadmapEditor.getCreatedAt());
        mapEditorDTO.setUpdatedAt(roadmapEditor.getUpdatedAt());
        mapEditorDTO.setDeletedAt(roadmapEditor.getDeletedAt());
        mapEditorDTO.setMap(roadmapEditor.getRoadmap() == null ? null : roadmapEditor.getRoadmap().getId());
        mapEditorDTO.setMember(roadmapEditor.getMember() == null ? null : roadmapEditor.getMember().getId());
        mapEditorDTO.setInvitedBy(
            roadmapEditor.getInvitedBy() == null ? null : roadmapEditor.getInvitedBy().getId());
        return mapEditorDTO;
    }

    private RoadmapEditor mapToEntity(final RoadmapEditorDTO mapEditorDTO, final RoadmapEditor roadmapEditor) {
        roadmapEditor.setPermission(mapEditorDTO.getPermission());
        roadmapEditor.setCreatedAt(mapEditorDTO.getCreatedAt());
        roadmapEditor.setUpdatedAt(mapEditorDTO.getUpdatedAt());
        roadmapEditor.setDeletedAt(mapEditorDTO.getDeletedAt());
        final Roadmap roadmap = mapEditorDTO.getMap() == null ? null : roadmapRepository.findById(mapEditorDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        roadmapEditor.setRoadmap(roadmap);
        final Member member = mapEditorDTO.getMember() == null ? null : memberRepository.findById(mapEditorDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        roadmapEditor.setMember(member);
        final Member invitedBy = mapEditorDTO.getInvitedBy() == null ? null : memberRepository.findById(mapEditorDTO.getInvitedBy())
                .orElseThrow(() -> new NotFoundException("invitedBy not found"));
        roadmapEditor.setInvitedBy(invitedBy);
        return roadmapEditor;
    }

}
