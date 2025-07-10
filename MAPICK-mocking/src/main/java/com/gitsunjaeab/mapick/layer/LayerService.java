package com.gitsunjaeab.mapick.layer;

import com.gitsunjaeab.mapick.layer.dto.LayerDTO;
import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.layer_library.LayerLibraryRepository;
import com.gitsunjaeab.mapick.layer_library.entity.LayerLibrary;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.marker.entity.Marker;
import com.gitsunjaeab.mapick.marker.MarkerRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public List<LayerDTO> findAll() {
        final List<Layer> layers = layerRepository.findAll(Sort.by("id"));
        return layers.stream()
            .map(layer -> roadmapToDTO(layer, new LayerDTO()))
            .toList();
    }

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