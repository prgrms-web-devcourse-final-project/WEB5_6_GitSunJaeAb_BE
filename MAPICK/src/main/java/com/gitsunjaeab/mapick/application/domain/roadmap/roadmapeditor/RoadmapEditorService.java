package com.gitsunjaeab.mapick.application.domain.roadmap.roadmapeditor;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapEditorDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapEditorSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.infra.common.EntityFinder;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 공유지도
@Service
@RequiredArgsConstructor
public class RoadmapEditorService {

    private final RoadmapEditorRepository roadmapEditorRepository;
    private final EntityFinder entityFinder;

    public List<RoadmapEditorDTO> findAll() {
        final List<RoadmapEditor> roadmapEditors = roadmapEditorRepository.findAll(Sort.by("id"));
        return roadmapEditors.stream()
                .map(roadmapEditor -> entityToDTO(roadmapEditor, new RoadmapEditorDTO()))
                .toList();
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

            Roadmap roadmap = entityFinder.findRoadmapById(roadmapId);

            Member member = entityFinder.findMemberById(memberId);

            RoadmapEditor editor = new RoadmapEditor();
            editor.setRoadmap(roadmap);
            editor.setMember(member);
            editor.setPermission("EDIT"); // 기본 권한
            editor.setCreatedAt(OffsetDateTime.now());

            roadmapEditorRepository.save(editor);
        }
    }

    private RoadmapEditorDTO entityToDTO(final RoadmapEditor roadmapEditor, final RoadmapEditorDTO roadmapEditorDTO) {
        roadmapEditorDTO.setId(roadmapEditor.getId());
        roadmapEditorDTO.setPermission(roadmapEditor.getPermission());
        roadmapEditorDTO.setCreatedAt(roadmapEditor.getCreatedAt());
        roadmapEditorDTO.setDeletedAt(roadmapEditor.getDeletedAt());
        roadmapEditorDTO.setRoadmap(roadmapEditor.getRoadmap() == null ? null : roadmapEditor.getRoadmap().getId());
        roadmapEditorDTO.setMember(roadmapEditor.getMember() == null ? null : roadmapEditor.getMember().getId());
        return roadmapEditorDTO;
    }

    private RoadmapEditor dtoToEntity(final RoadmapEditorDTO roadmapEditorDTO, final RoadmapEditor roadmapEditor) {
        roadmapEditor.setPermission(roadmapEditorDTO.getPermission());
        roadmapEditor.setCreatedAt(roadmapEditorDTO.getCreatedAt());
        roadmapEditor.setDeletedAt(roadmapEditorDTO.getDeletedAt());
        final Roadmap roadmap = roadmapEditorDTO.getRoadmap() == null ? null : entityFinder.findRoadmapById(roadmapEditorDTO.getRoadmap());

        roadmapEditor.setRoadmap(roadmap);
        final Member member = roadmapEditorDTO.getMember() == null ? null : entityFinder.findMemberById(roadmapEditorDTO.getMember());

        roadmapEditor.setMember(member);

        return roadmapEditor;
    }

}
