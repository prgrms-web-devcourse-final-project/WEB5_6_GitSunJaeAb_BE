package com.gitsunjaeab.mapick.category;

import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelation;
import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelationRepository;
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
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryResource {

    private final CategoryService categoryService;
    private final MapCategoryRelationRepository mapCategoryRelationRepository;

    public CategoryResource(final CategoryService categoryService,
            final MapCategoryRelationRepository mapCategoryRelationRepository) {
        this.categoryService = categoryService;
        this.mapCategoryRelationRepository = mapCategoryRelationRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCategory(@RequestBody @Valid final CategoryDTO categoryDTO) {
        final Long createdId = categoryService.create(categoryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategory(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CategoryDTO categoryDTO) {
        categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = categoryService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mapCategoryRelationsValues")
    public ResponseEntity<Map<Long, Long>> getMapCategoryRelationsValues() {
        return ResponseEntity.ok(mapCategoryRelationRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(MapCategoryRelation::getId, MapCategoryRelation::getId)));
    }

}
