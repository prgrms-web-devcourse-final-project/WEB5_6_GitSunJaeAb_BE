package com.gitsunjaeab.mapick.map_category_relation;

import com.gitsunjaeab.mapick.map.MapRepository;
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
@RequestMapping(value = "/api/mapCategoryRelations", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapCategoryRelationResource {

    private final MapCategoryRelationService mapCategoryRelationService;
    private final MapRepository mapRepository;

    public MapCategoryRelationResource(final MapCategoryRelationService mapCategoryRelationService,
            final MapRepository mapRepository) {
        this.mapCategoryRelationService = mapCategoryRelationService;
        this.mapRepository = mapRepository;
    }

    @GetMapping
    public ResponseEntity<List<MapCategoryRelationDTO>> getAllMapCategoryRelations() {
        return ResponseEntity.ok(mapCategoryRelationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapCategoryRelationDTO> getMapCategoryRelation(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(mapCategoryRelationService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMapCategoryRelation(
            @RequestBody @Valid final MapCategoryRelationDTO mapCategoryRelationDTO) {
        final Long createdId = mapCategoryRelationService.create(mapCategoryRelationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMapCategoryRelation(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MapCategoryRelationDTO mapCategoryRelationDTO) {
        mapCategoryRelationService.update(id, mapCategoryRelationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMapCategoryRelation(
            @PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = mapCategoryRelationService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        mapCategoryRelationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mapValues")
    public ResponseEntity<Map<Long, String>> getMapValues() {
        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.Map::getId, com.gitsunjaeab.mapick.map.Map::getTitle)));
    }

}
