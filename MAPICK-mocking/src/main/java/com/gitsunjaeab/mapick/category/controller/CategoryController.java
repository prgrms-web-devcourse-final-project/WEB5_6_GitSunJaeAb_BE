package com.gitsunjaeab.mapick.category.controller;

import com.gitsunjaeab.mapick.category.CategoryService;
import com.gitsunjaeab.mapick.category.dto.CategoryDTO;
import com.gitsunjaeab.mapick.roadmap_category_relation.RoadmapCategoryRelationRepository;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;
    private final RoadmapCategoryRelationRepository roadmapCategoryRelationRepository;

    public CategoryController(CategoryService categoryService,
        RoadmapCategoryRelationRepository roadmapCategoryRelationRepository) {
        this.categoryService = categoryService;
        this.roadmapCategoryRelationRepository = roadmapCategoryRelationRepository;
    }

    // 전체 카테고리 조회
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CategoryDTO> getCategory(@PathVariable(name = "id") final Long id) {
//        return ResponseEntity.ok(categoryService.get(id));
//    }

//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createCategory(@RequestBody @Valid final CategoryDTO categoryDTO) {
//        final Long createdId = categoryService.create(categoryDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Long> updateCategory(@PathVariable(name = "id") final Long id,
//            @RequestBody @Valid final CategoryDTO categoryDTO) {
//        categoryService.update(id, categoryDTO);
//        return ResponseEntity.ok(id);
//    }
//
//    @DeleteMapping("/{id}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") final Long id) {
//        final ReferencedWarning referencedWarning = categoryService.getReferencedWarning(id);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        categoryService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/mapCategoryRelationsValues")
//    public ResponseEntity<Map<Long, Long>> getMapCategoryRelationsValues() {
//        return ResponseEntity.ok(mapCategoryRelationRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(
//                    CustomCollectors.toSortedMap(MapCategoryRelation::getId, MapCategoryRelation::getId)));
//    }

}
