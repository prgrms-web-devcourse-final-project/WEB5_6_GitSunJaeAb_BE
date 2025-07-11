package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerLibraryDTO;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LayerLibraryService {

    private final LayerLibraryRepository layerLibraryRepository;

    public LayerLibraryService(final LayerLibraryRepository layerLibraryRepository) {
        this.layerLibraryRepository = layerLibraryRepository;
    }

    public List<LayerLibraryDTO> findAll() {
        final List<LayerLibrary> layerLibraries = layerLibraryRepository.findAll(Sort.by("id"));
        return layerLibraries.stream()
                .map(layerLibrary -> roadmapToDTO(layerLibrary, new LayerLibraryDTO()))
                .toList();
    }

    private LayerLibraryDTO roadmapToDTO(final LayerLibrary layerLibrary,
            final LayerLibraryDTO layerLibraryDTO) {
        layerLibraryDTO.setId(layerLibrary.getId());
        layerLibraryDTO.setCreatedAt(layerLibrary.getCreatedAt());
        layerLibraryDTO.setMember(layerLibrary.getMember() == null ? null : layerLibrary.getMember().getId());
        layerLibraryDTO.setLayer(layerLibrary.getLayer() == null ? null : layerLibrary.getLayer().getId());
        return layerLibraryDTO;
    }
}
