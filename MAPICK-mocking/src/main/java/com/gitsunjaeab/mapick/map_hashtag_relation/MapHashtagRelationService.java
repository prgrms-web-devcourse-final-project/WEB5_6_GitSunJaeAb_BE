package com.gitsunjaeab.mapick.map_hashtag_relation;

import com.gitsunjaeab.mapick.hashtag.Hashtag;
import com.gitsunjaeab.mapick.hashtag.HashtagRepository;
import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.map_hashtag_relation.dto.MapHashtagRelationDTO;
import com.gitsunjaeab.mapick.map_hashtag_relation.entity.MapHashtagRelation;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MapHashtagRelationService {

    private final MapHashtagRelationRepository mapHashtagRelationRepository;
    private final HashtagRepository hashtagRepository;
    private final MapRepository mapRepository;

    public MapHashtagRelationService(
            final MapHashtagRelationRepository mapHashtagRelationRepository,
            final HashtagRepository hashtagRepository, final MapRepository mapRepository) {
        this.mapHashtagRelationRepository = mapHashtagRelationRepository;
        this.hashtagRepository = hashtagRepository;
        this.mapRepository = mapRepository;
    }

    public List<MapHashtagRelationDTO> findAll() {
        final List<MapHashtagRelation> mapHashtagRelations = mapHashtagRelationRepository.findAll(Sort.by("id"));
        return mapHashtagRelations.stream()
                .map(mapHashtagRelation -> mapToDTO(mapHashtagRelation, new MapHashtagRelationDTO()))
                .toList();
    }

    public MapHashtagRelationDTO get(final Long id) {
        return mapHashtagRelationRepository.findById(id)
                .map(mapHashtagRelation -> mapToDTO(mapHashtagRelation, new MapHashtagRelationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MapHashtagRelationDTO mapHashtagRelationDTO) {
        final MapHashtagRelation mapHashtagRelation = new MapHashtagRelation();
        mapToEntity(mapHashtagRelationDTO, mapHashtagRelation);
        return mapHashtagRelationRepository.save(mapHashtagRelation).getId();
    }

    public void update(final Long id, final MapHashtagRelationDTO mapHashtagRelationDTO) {
        final MapHashtagRelation mapHashtagRelation = mapHashtagRelationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(mapHashtagRelationDTO, mapHashtagRelation);
        mapHashtagRelationRepository.save(mapHashtagRelation);
    }

    public void delete(final Long id) {
        mapHashtagRelationRepository.deleteById(id);
    }

    private MapHashtagRelationDTO mapToDTO(final MapHashtagRelation mapHashtagRelation,
            final MapHashtagRelationDTO mapHashtagRelationDTO) {
        mapHashtagRelationDTO.setId(mapHashtagRelation.getId());
        mapHashtagRelationDTO.setCreatedAt(mapHashtagRelation.getCreatedAt());
        mapHashtagRelationDTO.setHashtag(mapHashtagRelation.getHashtag() == null ? null : mapHashtagRelation.getHashtag().getId());
        mapHashtagRelationDTO.setMap(mapHashtagRelation.getMap() == null ? null : mapHashtagRelation.getMap().getId());
        return mapHashtagRelationDTO;
    }

    private MapHashtagRelation mapToEntity(final MapHashtagRelationDTO mapHashtagRelationDTO,
            final MapHashtagRelation mapHashtagRelation) {
        mapHashtagRelation.setCreatedAt(mapHashtagRelationDTO.getCreatedAt());
        final Hashtag hashtag = mapHashtagRelationDTO.getHashtag() == null ? null : hashtagRepository.findById(mapHashtagRelationDTO.getHashtag())
                .orElseThrow(() -> new NotFoundException("hashtag not found"));
        mapHashtagRelation.setHashtag(hashtag);
        final Map map = mapHashtagRelationDTO.getMap() == null ? null : mapRepository.findById(mapHashtagRelationDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        mapHashtagRelation.setMap(map);
        return mapHashtagRelation;
    }

}
