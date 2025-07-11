package com.gitsunjaeab.mapick.api.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryListResponse;
import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.api.category.dto.CategoryResponse;
import com.gitsunjaeab.mapick.application.category.CategoryService;
import com.gitsunjaeab.mapick.domain.category.Category;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
//    @GetMapping("/list")
    public ResponseEntity<CategoryListResponse> getCategoryList() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // 특정 카테고리 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok((categoryService.findById(categoryId)));
    }

    // 카테고리 생성 (관리자)
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<CategoryResponse> createCategory(
        @RequestBody @Valid final CategoryRequest request) {
        Category savedCategory = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.of(savedCategory));
    }


    // 카테고리 수정 (관리자)
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
        @PathVariable(name = "categoryId") Long categoryId,
        @RequestBody @Valid CategoryRequest request
    ) {
        Category updated = categoryService.update(categoryId, request);
        return ResponseEntity.ok(CategoryResponse.of(updated));
    }

    // 카테고리 삭제는 구현하지않음 (카테고리 삭제 시 연관된 로드맵 처리 문제 발생)

}
