package com.gitsunjaeab.mapick.roadmap_category_relation.controller;

import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.roadmap_category_relation.RoadmapCategoryRelationService;
import com.gitsunjaeab.mapick.roadmap_category_relation.dto.RoadmapCategoryRelationDTO;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
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
@RequestMapping(value = "/roadmapCategoryRelations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoadmapCategoryRelationController {

    private final RoadmapCategoryRelationService roadmapCategoryRelationService;
    private final RoadmapRepository roadmapRepository;

    public RoadmapCategoryRelationController(final RoadmapCategoryRelationService roadmapCategoryRelationService,
            final RoadmapRepository roadmapRepository) {
        this.roadmapCategoryRelationService = roadmapCategoryRelationService;
        this.roadmapRepository = roadmapRepository;
    }

    @GetMapping
    public ResponseEntity<List<RoadmapCategoryRelationDTO>> getAllRoadmapCategoryRelations() {
        return ResponseEntity.ok(roadmapCategoryRelationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoadmapCategoryRelationDTO> getRoadmapCategoryRelation(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roadmapCategoryRelationService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRoadmapCategoryRelation(
            @RequestBody @Valid final RoadmapCategoryRelationDTO mapCategoryRelationDTO) {
        final Long createdId = roadmapCategoryRelationService.create(mapCategoryRelationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoadmapCategoryRelation(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoadmapCategoryRelationDTO mapCategoryRelationDTO) {
        roadmapCategoryRelationService.update(id, mapCategoryRelationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoadmapCategoryRelation(
            @PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = roadmapCategoryRelationService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        roadmapCategoryRelationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roadmapValues")
    public ResponseEntity<Map<Long, String>> getRoadmapValues() {
        return ResponseEntity.ok(roadmapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedRoadmap(com.gitsunjaeab.mapick.roadmap.entity.Roadmap::getId, com.gitsunjaeab.mapick.roadmap.entity.Roadmap::getTitle)));
    }

}
