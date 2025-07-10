package com.gitsunjaeab.mapick.map_hashtag_relation.controller;

import com.gitsunjaeab.mapick.hashtag.Hashtag;
import com.gitsunjaeab.mapick.hashtag.HashtagRepository;
import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.map_hashtag_relation.dto.MapHashtagRelationDTO;
import com.gitsunjaeab.mapick.map_hashtag_relation.MapHashtagRelationService;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/mapHashtagRelations", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapHashtagRelationController {

    private final MapHashtagRelationService mapHashtagRelationService;
    private final HashtagRepository hashtagRepository;
    private final MapRepository mapRepository;

    public MapHashtagRelationController(final MapHashtagRelationService mapHashtagRelationService,
            final HashtagRepository hashtagRepository, final MapRepository mapRepository) {
        this.mapHashtagRelationService = mapHashtagRelationService;
        this.hashtagRepository = hashtagRepository;
        this.mapRepository = mapRepository;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMapHashtagRelation(
        @RequestBody @Valid final MapHashtagRelationDTO mapHashtagRelationDTO) {
        final Long createdId = mapHashtagRelationService.create(mapHashtagRelationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MapHashtagRelationDTO>> getAllMapHashtagRelations() {
        return ResponseEntity.ok(mapHashtagRelationService.findAll());
    }

    @GetMapping("/{mapHashtagRelationsId}")
    public ResponseEntity<MapHashtagRelationDTO> getMapHashtagRelation(
            @PathVariable(name = "mapHashtagRelationsId") final Long mapHashtagRelationsId) {
        return ResponseEntity.ok(mapHashtagRelationService.get(mapHashtagRelationsId));
    }

    @PutMapping("/{mapHashtagRelationsId}")
    public ResponseEntity<Long> updateMapHashtagRelation(@PathVariable(name = "mapHashtagRelationsId") final Long mapHashtagRelationsId,
            @RequestBody @Valid final MapHashtagRelationDTO mapHashtagRelationDTO) {
        mapHashtagRelationService.update(mapHashtagRelationsId, mapHashtagRelationDTO);
        return ResponseEntity.ok(mapHashtagRelationsId);
    }

    @DeleteMapping("/{mapHashtagRelationsId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMapHashtagRelation(@PathVariable(name = "mapHashtagRelationsId") final Long mapHashtagRelationsId) {
        mapHashtagRelationService.delete(mapHashtagRelationsId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/hashtagValues")
//    public ResponseEntity<Map<Long, String>> getHashtagValues() {
//        return ResponseEntity.ok(hashtagRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Hashtag::getId, Hashtag::getName)));
//    }
//
//    @GetMapping("/mapValues")
//    public ResponseEntity<Map<Long, String>> getMapValues() {
//        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.Map::getId, com.gitsunjaeab.mapick.map.Map::getTitle)));
//    }

}
