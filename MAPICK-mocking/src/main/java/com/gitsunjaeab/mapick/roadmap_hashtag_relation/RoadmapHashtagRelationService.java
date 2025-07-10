package com.gitsunjaeab.mapick.roadmap_hashtag_relation;

import com.gitsunjaeab.mapick.hashtag.entity.Hashtag;
import com.gitsunjaeab.mapick.hashtag.HashtagRepository;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.dto.RoadmapHashtagRelationDTO;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.entity.RoadmapHashtagRelation;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RoadmapHashtagRelationService {

    private final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository;
    private final HashtagRepository hashtagRepository;
    private final RoadmapRepository roadmapRepository;

    public RoadmapHashtagRelationService(
            final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository,
            final HashtagRepository hashtagRepository, final RoadmapRepository roadmapRepository) {
        this.roadmapHashtagRelationRepository = roadmapHashtagRelationRepository;
        this.hashtagRepository = hashtagRepository;
        this.roadmapRepository = roadmapRepository;
    }

    public List<RoadmapHashtagRelationDTO> findAll() {
        final List<RoadmapHashtagRelation> roadmapHashtagRelations = roadmapHashtagRelationRepository.findAll(Sort.by("id"));
        return roadmapHashtagRelations.stream()
                .map(roadmapHashtagRelation -> mapToDTO(roadmapHashtagRelation, new RoadmapHashtagRelationDTO()))
                .toList();
    }

    public RoadmapHashtagRelationDTO get(final Long id) {
        return roadmapHashtagRelationRepository.findById(id)
                .map(mapHashtagRelation -> mapToDTO(mapHashtagRelation, new RoadmapHashtagRelationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        final RoadmapHashtagRelation roadmapHashtagRelation = new RoadmapHashtagRelation();
        mapToEntity(roadmapHashtagRelationDTO, roadmapHashtagRelation);
        return roadmapHashtagRelationRepository.save(roadmapHashtagRelation).getId();
    }

    public void update(final Long id, final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        final RoadmapHashtagRelation roadmapHashtagRelation = roadmapHashtagRelationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roadmapHashtagRelationDTO, roadmapHashtagRelation);
        roadmapHashtagRelationRepository.save(roadmapHashtagRelation);
    }

    public void delete(final Long id) {
        roadmapHashtagRelationRepository.deleteById(id);
    }

    private RoadmapHashtagRelationDTO mapToDTO(final RoadmapHashtagRelation roadmapHashtagRelation,
            final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        roadmapHashtagRelationDTO.setId(roadmapHashtagRelation.getId());
        roadmapHashtagRelationDTO.setCreatedAt(roadmapHashtagRelation.getCreatedAt());
        roadmapHashtagRelationDTO.setHashtag(
            roadmapHashtagRelation.getHashtag() == null ? null : roadmapHashtagRelation.getHashtag().getId());
        roadmapHashtagRelationDTO.setMap(
            roadmapHashtagRelation.getRoadmap() == null ? null : roadmapHashtagRelation.getRoadmap().getId());
        return roadmapHashtagRelationDTO;
    }

    private RoadmapHashtagRelation mapToEntity(final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO,
            final RoadmapHashtagRelation roadmapHashtagRelation) {
        roadmapHashtagRelation.setCreatedAt(roadmapHashtagRelationDTO.getCreatedAt());
        final Hashtag hashtag = roadmapHashtagRelationDTO.getHashtag() == null ? null : hashtagRepository.findById(
                roadmapHashtagRelationDTO.getHashtag())
                .orElseThrow(() -> new NotFoundException("hashtag not found"));
        roadmapHashtagRelation.setHashtag(hashtag);
        final Roadmap roadmap = roadmapHashtagRelationDTO.getMap() == null ? null : roadmapRepository.findById(
                roadmapHashtagRelationDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        roadmapHashtagRelation.setRoadmap(roadmap);
        return roadmapHashtagRelation;
    }

}
