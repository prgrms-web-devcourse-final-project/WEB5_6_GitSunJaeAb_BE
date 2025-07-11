package com.gitsunjaeab.mapick.api.category;

import com.gitsunjaeab.mapick.application.category.CategoryService;
import com.gitsunjaeab.mapick.api.category.dto.CategoryDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 전체 카테고리 조회
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // 카테고리 생성 (관리자)
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCategory(@RequestBody @Valid final CategoryDTO categoryDTO) {
        final Long createdId = categoryService.create(categoryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    // 카테고리 수정 (관리자)
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategory(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final CategoryDTO categoryDTO) {
        categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(id);
    }
}
