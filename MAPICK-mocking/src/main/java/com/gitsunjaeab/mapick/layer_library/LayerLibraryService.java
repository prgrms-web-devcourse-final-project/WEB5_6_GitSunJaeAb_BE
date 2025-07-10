package com.gitsunjaeab.mapick.layer_library;

import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.layer.LayerRepository;
import com.gitsunjaeab.mapick.layer_library.dto.LayerLibraryDTO;
import com.gitsunjaeab.mapick.layer_library.entity.LayerLibrary;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LayerLibraryService {

    private final LayerLibraryRepository layerLibraryRepository;
    private final MemberRepository memberRepository;
    private final LayerRepository layerRepository;

    public LayerLibraryService(final LayerLibraryRepository layerLibraryRepository,
            final MemberRepository memberRepository, final LayerRepository layerRepository) {
        this.layerLibraryRepository = layerLibraryRepository;
        this.memberRepository = memberRepository;
        this.layerRepository = layerRepository;
    }

    public List<LayerLibraryDTO> findAll() {
        final List<LayerLibrary> layerLibraries = layerLibraryRepository.findAll(Sort.by("id"));
        return layerLibraries.stream()
                .map(layerLibrary -> mapToDTO(layerLibrary, new LayerLibraryDTO()))
                .toList();
    }

    public LayerLibraryDTO get(final Long id) {
        return layerLibraryRepository.findById(id)
                .map(layerLibrary -> mapToDTO(layerLibrary, new LayerLibraryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LayerLibraryDTO layerLibraryDTO) {
        final LayerLibrary layerLibrary = new LayerLibrary();
        mapToEntity(layerLibraryDTO, layerLibrary);
        return layerLibraryRepository.save(layerLibrary).getId();
    }

    public void update(final Long id, final LayerLibraryDTO layerLibraryDTO) {
        final LayerLibrary layerLibrary = layerLibraryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(layerLibraryDTO, layerLibrary);
        layerLibraryRepository.save(layerLibrary);
    }

    public void delete(final Long id) {
        layerLibraryRepository.deleteById(id);
    }

    private LayerLibraryDTO mapToDTO(final LayerLibrary layerLibrary,
            final LayerLibraryDTO layerLibraryDTO) {
        layerLibraryDTO.setId(layerLibrary.getId());
        layerLibraryDTO.setCreatedAt(layerLibrary.getCreatedAt());
        layerLibraryDTO.setMember(layerLibrary.getMember() == null ? null : layerLibrary.getMember().getId());
        layerLibraryDTO.setLayer(layerLibrary.getLayer() == null ? null : layerLibrary.getLayer().getId());
        return layerLibraryDTO;
    }

    private LayerLibrary mapToEntity(final LayerLibraryDTO layerLibraryDTO,
            final LayerLibrary layerLibrary) {
        layerLibrary.setCreatedAt(layerLibraryDTO.getCreatedAt());
        final Member member = layerLibraryDTO.getMember() == null ? null : memberRepository.findById(layerLibraryDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        layerLibrary.setMember(member);
        final Layer layer = layerLibraryDTO.getLayer() == null ? null : layerRepository.findById(layerLibraryDTO.getLayer())
                .orElseThrow(() -> new NotFoundException("layer not found"));
        layerLibrary.setLayer(layer);
        return layerLibrary;
    }

}
