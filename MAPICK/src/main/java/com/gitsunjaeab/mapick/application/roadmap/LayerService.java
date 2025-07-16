package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LayerService {

    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final LayerLibraryRepository layerLibraryRepository;

    public LayerService(final LayerRepository layerRepository,
        final MemberRepository memberRepository, final RoadmapRepository roadmapRepository,
        final MarkerRepository markerRepository,
        final LayerLibraryRepository layerLibraryRepository) {
        this.layerRepository = layerRepository;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.markerRepository = markerRepository;
        this.layerLibraryRepository = layerLibraryRepository;
    }

    @Transactional(readOnly = true)
    public LayerListResponse findAllLayersOnRoadmap(Long roadmapId) {
        final List<Layer> layers = layerRepository.findAllByRoadmap_Id(roadmapId);
        return LayerListResponse.of(layers);
    }

    @Transactional(readOnly = true)
    public LayerListResponse findAllMemberLayers(Long memberId) {
        List<Long> layerIds = layerLibraryRepository.findLayerIdsByMemberId(memberId);

        if (layerIds.isEmpty()) {
            return LayerListResponse.of(Collections.emptyList());
        }

        List<Layer> layers = layerRepository.findAllById(layerIds);
        return LayerListResponse.of(layers);
    }

    @Transactional(readOnly = true)
    public LayerDTO get(final Long id) {
        return layerRepository.findById(id)
            .map(layer -> roadmapToDTO(layer, new LayerDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final LayerDTO layerDTO) {
        final Layer layer = new Layer();
        roadmapToEntity(layerDTO, layer);
        return layerRepository.save(layer).getId();
    }

    public void update(final Long id, final LayerDTO layerDTO) {
        final Layer layer = layerRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        roadmapToEntity(layerDTO, layer);
        layerRepository.save(layer);
    }

    public void delete(final Long id) {
        layerRepository.deleteById(id);
    }

    private LayerDTO roadmapToDTO(final Layer layer, final LayerDTO layerDTO) {
        layerDTO.setId(layer.getId());
        layerDTO.setName(layer.getName());
        layerDTO.setDescription(layer.getDescription());
        layerDTO.setLayerSeq(layer.getLayerSeq());
        layerDTO.setLayerTime(layer.getLayerTime());
        layerDTO.setCreatedAt(layer.getCreatedAt());
        layerDTO.setUpdatedAt(layer.getUpdatedAt());
        layerDTO.setDeletedAt(layer.getDeletedAt());
        layerDTO.setMember(layer.getMember() == null ? null : layer.getMember().getId());
        layerDTO.setRoadmap(layer.getRoadmap() == null ? null : layer.getRoadmap().getId());
        return layerDTO;
    }

    private Layer roadmapToEntity(final LayerDTO layerDTO, final Layer layer) {
        layer.setName(layerDTO.getName());
        layer.setDescription(layerDTO.getDescription());
        layer.setLayerSeq(layerDTO.getLayerSeq());
        layer.setLayerTime(layerDTO.getLayerTime());
        layer.setCreatedAt(layerDTO.getCreatedAt());
        layer.setUpdatedAt(layerDTO.getUpdatedAt());
        layer.setDeletedAt(layerDTO.getDeletedAt());
        final Member member = layerDTO.getMember() == null ? null : memberRepository.findById(layerDTO.getMember())
            .orElseThrow(() -> new NotFoundException("member not found"));
        layer.setMember(member);
        final Roadmap roadmap = layerDTO.getRoadmap() == null ? null : roadmapRepository.findById(layerDTO.getRoadmap())
            .orElseThrow(() -> new NotFoundException("map not found"));
        layer.setRoadmap(roadmap);
        return layer;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Layer layer = layerRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        final Marker layerMarker = markerRepository.findFirstByLayer(layer);
        if (layerMarker != null) {
            referencedWarning.setKey("layer.marker.layer.referenced");
            referencedWarning.addParam(layerMarker.getId());
            return referencedWarning;
        }
        final LayerLibrary layerLayerLibrary = layerLibraryRepository.findFirstByLayer(layer);
        if (layerLayerLibrary != null) {
            referencedWarning.setKey("layer.layerLibrary.layer.referenced");
            referencedWarning.addParam(layerLayerLibrary.getId());
            return referencedWarning;
        }
        return null;
    }
}