package com.gitsunjaeab.mapick.roadmap_hashtag_relation.controller;

import com.gitsunjaeab.mapick.hashtag.entity.Hashtag;
import com.gitsunjaeab.mapick.hashtag.HashtagRepository;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.RoadmapHashtagRelationService;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.dto.RoadmapHashtagRelationDTO;
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
@RequestMapping(value = "/roadmapHashtagRelations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoadmapHashtagRelationController {

    private final RoadmapHashtagRelationService roadmapHashtagRelationService;
    private final HashtagRepository hashtagRepository;
    private final RoadmapRepository roadmapRepository;

    public RoadmapHashtagRelationController(final RoadmapHashtagRelationService roadmapHashtagRelationService,
            final HashtagRepository hashtagRepository, final RoadmapRepository roadmapRepository) {
        this.roadmapHashtagRelationService = roadmapHashtagRelationService;
        this.hashtagRepository = hashtagRepository;
        this.roadmapRepository = roadmapRepository;
    }

    @GetMapping
    public ResponseEntity<List<RoadmapHashtagRelationDTO>> getAllRoadmapHashtagRelations() {
        return ResponseEntity.ok(roadmapHashtagRelationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoadmapHashtagRelationDTO> getRoadmapHashtagRelation(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roadmapHashtagRelationService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRoadmapHashtagRelation(
            @RequestBody @Valid final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        final Long createdId = roadmapHashtagRelationService.create(roadmapHashtagRelationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoadmapHashtagRelation(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoadmapHashtagRelationDTO roadmapHashtagRelationDTO) {
        roadmapHashtagRelationService.update(id, roadmapHashtagRelationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoadmapHashtagRelation(@PathVariable(name = "id") final Long id) {
        roadmapHashtagRelationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hashtagValues")
    public ResponseEntity<Map<Long, String>> getHashtagValues() {
        return ResponseEntity.ok(hashtagRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedRoadmap(Hashtag::getId, Hashtag::getName)));
    }

    @GetMapping("/mapValues")
    public ResponseEntity<Map<Long, String>> getRoadmapValues() {
        return ResponseEntity.ok(roadmapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedRoadmap(Roadmap::getId, Roadmap::getTitle)));
    }

}
