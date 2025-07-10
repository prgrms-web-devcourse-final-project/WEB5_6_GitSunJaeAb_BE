package com.gitsunjaeab.mapick.roadmap_category_relation;

import com.gitsunjaeab.mapick.category.entity.Category;
import com.gitsunjaeab.mapick.category.CategoryRepository;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.roadmap_category_relation.dto.RoadmapCategoryRelationDTO;
import com.gitsunjaeab.mapick.roadmap_category_relation.entity.RoadmapCategoryRelation;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RoadmapCategoryRelationService {

    private final RoadmapCategoryRelationRepository roadmapCategoryRelationRepository;
    private final RoadmapRepository roadmapRepository;
    private final CategoryRepository categoryRepository;

    public RoadmapCategoryRelationService(
            final RoadmapCategoryRelationRepository roadmapCategoryRelationRepository,
            final RoadmapRepository roadmapRepository, final CategoryRepository categoryRepository) {
        this.roadmapCategoryRelationRepository = roadmapCategoryRelationRepository;
        this.roadmapRepository = roadmapRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<RoadmapCategoryRelationDTO> findAll() {
        final List<RoadmapCategoryRelation> roadmapCategoryRelations = roadmapCategoryRelationRepository.findAll(Sort.by("id"));
        return roadmapCategoryRelations.stream()
                .map(roadmapCategoryRelation -> roadmapToDTO(roadmapCategoryRelation, new RoadmapCategoryRelationDTO()))
                .toList();
    }

    public RoadmapCategoryRelationDTO get(final Long id) {
        return roadmapCategoryRelationRepository.findById(id)
                .map(roadmapCategoryRelation -> roadmapToDTO(roadmapCategoryRelation, new RoadmapCategoryRelationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoadmapCategoryRelationDTO roadmapCategoryRelationDTO) {
        final RoadmapCategoryRelation roadmapCategoryRelation = new RoadmapCategoryRelation();
        roadmapToEntity(roadmapCategoryRelationDTO, roadmapCategoryRelation);
        return roadmapCategoryRelationRepository.save(roadmapCategoryRelation).getId();
    }

    public void update(final Long id, final RoadmapCategoryRelationDTO roadmapCategoryRelationDTO) {
        final RoadmapCategoryRelation roadmapCategoryRelation = roadmapCategoryRelationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(roadmapCategoryRelationDTO, roadmapCategoryRelation);
        roadmapCategoryRelationRepository.save(roadmapCategoryRelation);
    }

    public void delete(final Long id) {
        roadmapCategoryRelationRepository.deleteById(id);
    }

    private RoadmapCategoryRelationDTO roadmapToDTO(final RoadmapCategoryRelation roadmapCategoryRelation,
        final RoadmapCategoryRelationDTO roadmapCategoryRelationDTO) {
        roadmapCategoryRelationDTO.setId(roadmapCategoryRelation.getId());
        roadmapCategoryRelationDTO.setCreatedAt(roadmapCategoryRelation.getCreatedAt());
        roadmapCategoryRelationDTO.setRoadmap(
            roadmapCategoryRelation.getRoadmap() == null ? null : roadmapCategoryRelation.getRoadmap().getId()
        );
        return roadmapCategoryRelationDTO;
    }

    private RoadmapCategoryRelation roadmapToEntity(final RoadmapCategoryRelationDTO roadmapCategoryRelationDTO,
            final RoadmapCategoryRelation roadmapCategoryRelation) {
        roadmapCategoryRelation.setCreatedAt(roadmapCategoryRelationDTO.getCreatedAt());
        final Roadmap roadmap = roadmapCategoryRelationDTO.getRoadmap() == null ? null : roadmapRepository.findById(
                roadmapCategoryRelationDTO.getRoadmap())
                .orElseThrow(() -> new NotFoundException("roadmap not found"));
        roadmapCategoryRelation.setRoadmap(roadmap);
        return roadmapCategoryRelation;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final RoadmapCategoryRelation roadmapCategoryRelation = roadmapCategoryRelationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Category roadmapCategoryRelationsCategory = categoryRepository.findFirstByRoadmapCategoryRelations(
            roadmapCategoryRelation);
        if (roadmapCategoryRelationsCategory != null) {
            referencedWarning.setKey("roadmapCategoryRelation.category.roadmapCategoryRelations.referenced");
            referencedWarning.addParam(roadmapCategoryRelationsCategory.getId());
            return referencedWarning;
        }
        return null;
    }

}
