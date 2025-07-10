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
                .map(roadmapHashtagRelation -> roadmapToDTO(roadmapHashtagRelation, new RoadmapHashtagRelationDTO()))
                .toList();
    }

    public RoadmapHashtagRelationDTO get(final Long id) {
        return roadmapHashtagRelationRepository.findById(id)
                .map(mapHashtagRelation -> roadmapToDTO(mapHashtagRelation, new RoadmapHashtagRelationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        final RoadmapHashtagRelation roadmapHashtagRelation = new RoadmapHashtagRelation();
        roadmapToEntity(roadmapHashtagRelationDTO, roadmapHashtagRelation);
        return roadmapHashtagRelationRepository.save(roadmapHashtagRelation).getId();
    }

    public void update(final Long id, final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        final RoadmapHashtagRelation roadmapHashtagRelation = roadmapHashtagRelationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(roadmapHashtagRelationDTO, roadmapHashtagRelation);
        roadmapHashtagRelationRepository.save(roadmapHashtagRelation);
    }

    public void delete(final Long id) {
        roadmapHashtagRelationRepository.deleteById(id);
    }

    private RoadmapHashtagRelationDTO roadmapToDTO(final RoadmapHashtagRelation roadmapHashtagRelation,
            final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        roadmapHashtagRelationDTO.setId(roadmapHashtagRelation.getId());
        roadmapHashtagRelationDTO.setCreatedAt(roadmapHashtagRelation.getCreatedAt());
        roadmapHashtagRelationDTO.setHashtag(
            roadmapHashtagRelation.getHashtag() == null ? null : roadmapHashtagRelation.getHashtag().getId());
        roadmapHashtagRelationDTO.setRoadmap(
            roadmapHashtagRelation.getRoadmap() == null ? null : roadmapHashtagRelation.getRoadmap().getId());
        return roadmapHashtagRelationDTO;
    }

    private RoadmapHashtagRelation roadmapToEntity(final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO,
            final RoadmapHashtagRelation roadmapHashtagRelation) {
        roadmapHashtagRelation.setCreatedAt(roadmapHashtagRelationDTO.getCreatedAt());
        final Hashtag hashtag = roadmapHashtagRelationDTO.getHashtag() == null ? null : hashtagRepository.findById(
                roadmapHashtagRelationDTO.getHashtag())
                .orElseThrow(() -> new NotFoundException("hashtag not found"));
        roadmapHashtagRelation.setHashtag(hashtag);
        final Roadmap roadmap = roadmapHashtagRelationDTO.getRoadmap() == null ? null : roadmapRepository.findById(
                roadmapHashtagRelationDTO.getRoadmap())
                .orElseThrow(() -> new NotFoundException("roadmap not found"));
        roadmapHashtagRelation.setRoadmap(roadmap);
        return roadmapHashtagRelation;
    }

}
