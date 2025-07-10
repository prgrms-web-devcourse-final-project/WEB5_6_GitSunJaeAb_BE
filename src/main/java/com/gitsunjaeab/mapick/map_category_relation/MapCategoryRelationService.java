package com.gitsunjaeab.mapick.map_category_relation;

import com.gitsunjaeab.mapick.category.Category;
import com.gitsunjaeab.mapick.category.CategoryRepository;
import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MapCategoryRelationService {

    private final MapCategoryRelationRepository mapCategoryRelationRepository;
    private final MapRepository mapRepository;
    private final CategoryRepository categoryRepository;

    public MapCategoryRelationService(
            final MapCategoryRelationRepository mapCategoryRelationRepository,
            final MapRepository mapRepository, final CategoryRepository categoryRepository) {
        this.mapCategoryRelationRepository = mapCategoryRelationRepository;
        this.mapRepository = mapRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<MapCategoryRelationDTO> findAll() {
        final List<MapCategoryRelation> mapCategoryRelations = mapCategoryRelationRepository.findAll(Sort.by("id"));
        return mapCategoryRelations.stream()
                .map(mapCategoryRelation -> mapToDTO(mapCategoryRelation, new MapCategoryRelationDTO()))
                .toList();
    }

    public MapCategoryRelationDTO get(final Long id) {
        return mapCategoryRelationRepository.findById(id)
                .map(mapCategoryRelation -> mapToDTO(mapCategoryRelation, new MapCategoryRelationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MapCategoryRelationDTO mapCategoryRelationDTO) {
        final MapCategoryRelation mapCategoryRelation = new MapCategoryRelation();
        mapToEntity(mapCategoryRelationDTO, mapCategoryRelation);
        return mapCategoryRelationRepository.save(mapCategoryRelation).getId();
    }

    public void update(final Long id, final MapCategoryRelationDTO mapCategoryRelationDTO) {
        final MapCategoryRelation mapCategoryRelation = mapCategoryRelationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(mapCategoryRelationDTO, mapCategoryRelation);
        mapCategoryRelationRepository.save(mapCategoryRelation);
    }

    public void delete(final Long id) {
        mapCategoryRelationRepository.deleteById(id);
    }

    private MapCategoryRelationDTO mapToDTO(final MapCategoryRelation mapCategoryRelation,
            final MapCategoryRelationDTO mapCategoryRelationDTO) {
        mapCategoryRelationDTO.setId(mapCategoryRelation.getId());
        mapCategoryRelationDTO.setCreatedAt(mapCategoryRelation.getCreatedAt());
        mapCategoryRelationDTO.setMap(mapCategoryRelation.getMap() == null ? null : mapCategoryRelation.getMap().getId());
        return mapCategoryRelationDTO;
    }

    private MapCategoryRelation mapToEntity(final MapCategoryRelationDTO mapCategoryRelationDTO,
            final MapCategoryRelation mapCategoryRelation) {
        mapCategoryRelation.setCreatedAt(mapCategoryRelationDTO.getCreatedAt());
        final Map map = mapCategoryRelationDTO.getMap() == null ? null : mapRepository.findById(mapCategoryRelationDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        mapCategoryRelation.setMap(map);
        return mapCategoryRelation;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final MapCategoryRelation mapCategoryRelation = mapCategoryRelationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Category mapCategoryRelationsCategory = categoryRepository.findFirstByMapCategoryRelations(mapCategoryRelation);
        if (mapCategoryRelationsCategory != null) {
            referencedWarning.setKey("mapCategoryRelation.category.mapCategoryRelations.referenced");
            referencedWarning.addParam(mapCategoryRelationsCategory.getId());
            return referencedWarning;
        }
        return null;
    }

}
