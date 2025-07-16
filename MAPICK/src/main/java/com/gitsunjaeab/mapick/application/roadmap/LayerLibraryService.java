package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerLibraryDTO;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LayerLibraryService {

    private final LayerLibraryRepository layerLibraryRepository;
    private final LayerRepository layerRepository;

    public LayerLibraryService(final LayerLibraryRepository layerLibraryRepository,
        LayerRepository layerRepository) {
        this.layerLibraryRepository = layerLibraryRepository;
        this.layerRepository = layerRepository;
    }

    public List<LayerLibraryDTO> findAll() {
        final List<LayerLibrary> layerLibraries = layerLibraryRepository.findAll(Sort.by("id"));
        return layerLibraries.stream()
            .map(layerLibrary -> roadmapToDTO(layerLibrary, new LayerLibraryDTO()))
            .toList();
    }

    // 전체 회원 라이브러리 레이어 조회
    @Transactional(readOnly = true)
    public LayerListResponse findAllLibraryLayers() {
        List<Long> layerIds = layerLibraryRepository.findAllLayerIdsInLibrary();

        if (layerIds.isEmpty()) {
            return LayerListResponse.of(Collections.emptyList());
        }

        List<Layer> layers = layerRepository.findAllById(layerIds);
        return LayerListResponse.of(layers);
    }

    // 특정 회원의 레이어 찜 목록 조회
    @Transactional(readOnly = true)
    public LayerListResponse findAllMemberLayers(Long memberId) {
        List<Long> layerId = layerLibraryRepository.findLayerIdsByMemberId(memberId);

        if (layerId.isEmpty()) {
            return LayerListResponse.of(Collections.emptyList());
        }

        List<Layer> layers = layerRepository.findAllById(layerId);

        return LayerListResponse.of(layers);
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
