package com.gitsunjaeab.mapick.map_editor;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MapEditorService {

    private final MapEditorRepository mapEditorRepository;
    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;

    public MapEditorService(final MapEditorRepository mapEditorRepository,
            final MapRepository mapRepository, final MemberRepository memberRepository) {
        this.mapEditorRepository = mapEditorRepository;
        this.mapRepository = mapRepository;
        this.memberRepository = memberRepository;
    }

    public List<MapEditorDTO> findAll() {
        final List<MapEditor> mapEditors = mapEditorRepository.findAll(Sort.by("id"));
        return mapEditors.stream()
                .map(mapEditor -> mapToDTO(mapEditor, new MapEditorDTO()))
                .toList();
    }

    public MapEditorDTO get(final Long id) {
        return mapEditorRepository.findById(id)
                .map(mapEditor -> mapToDTO(mapEditor, new MapEditorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MapEditorDTO mapEditorDTO) {
        final MapEditor mapEditor = new MapEditor();
        mapToEntity(mapEditorDTO, mapEditor);
        return mapEditorRepository.save(mapEditor).getId();
    }

    public void update(final Long id, final MapEditorDTO mapEditorDTO) {
        final MapEditor mapEditor = mapEditorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(mapEditorDTO, mapEditor);
        mapEditorRepository.save(mapEditor);
    }

    public void delete(final Long id) {
        mapEditorRepository.deleteById(id);
    }

    private MapEditorDTO mapToDTO(final MapEditor mapEditor, final MapEditorDTO mapEditorDTO) {
        mapEditorDTO.setId(mapEditor.getId());
        mapEditorDTO.setPermission(mapEditor.getPermission());
        mapEditorDTO.setCreatedAt(mapEditor.getCreatedAt());
        mapEditorDTO.setUpdatedAt(mapEditor.getUpdatedAt());
        mapEditorDTO.setDeletedAt(mapEditor.getDeletedAt());
        mapEditorDTO.setMap(mapEditor.getMap() == null ? null : mapEditor.getMap().getId());
        mapEditorDTO.setMember(mapEditor.getMember() == null ? null : mapEditor.getMember().getId());
        mapEditorDTO.setInvitedBy(mapEditor.getInvitedBy() == null ? null : mapEditor.getInvitedBy().getId());
        return mapEditorDTO;
    }

    private MapEditor mapToEntity(final MapEditorDTO mapEditorDTO, final MapEditor mapEditor) {
        mapEditor.setPermission(mapEditorDTO.getPermission());
        mapEditor.setCreatedAt(mapEditorDTO.getCreatedAt());
        mapEditor.setUpdatedAt(mapEditorDTO.getUpdatedAt());
        mapEditor.setDeletedAt(mapEditorDTO.getDeletedAt());
        final Map map = mapEditorDTO.getMap() == null ? null : mapRepository.findById(mapEditorDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        mapEditor.setMap(map);
        final Member member = mapEditorDTO.getMember() == null ? null : memberRepository.findById(mapEditorDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        mapEditor.setMember(member);
        final Member invitedBy = mapEditorDTO.getInvitedBy() == null ? null : memberRepository.findById(mapEditorDTO.getInvitedBy())
                .orElseThrow(() -> new NotFoundException("invitedBy not found"));
        mapEditor.setInvitedBy(invitedBy);
        return mapEditor;
    }

}
