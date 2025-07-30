package com.gitsunjaeab.mapick.api.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryDTO;
import com.gitsunjaeab.mapick.api.category.dto.CategoryListResponse;
import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.api.category.dto.CategoryResponse;
import com.gitsunjaeab.mapick.api.category.dto.Top5CategoriesResponse;
import com.gitsunjaeab.mapick.application.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "카테고리 관리 API")
@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성", description = "[관리자] 카테고리 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
        @RequestParam final String name,
        @RequestParam final String description,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        final CategoryRequest request = new CategoryRequest(name, description);
        categoryService.create(request, imageFile);
        final CategoryResponse response = CategoryResponse.create();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 카테고리 조회", description = "[관리자/사용자용] 전체 카테고리 목록을 조회")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CategoryListResponse> getCategoryList()
    {
        final List<CategoryDTO> categoryDTOList  = categoryService.findAll();
        final CategoryListResponse response = CategoryListResponse.get(categoryDTOList);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "인기 카테고리 top5 조회", description = "[모든 사용자] 북마크 된 로드맵들의 카테고리 기준, 최대 상위 5개 조회")
    @GetMapping("/top5")
    public ResponseEntity<Top5CategoriesResponse> getTop5Categories()
    {
        final List<CategoryDTO> categoryDTOS = categoryService.getTop5List();
        final Top5CategoriesResponse response = Top5CategoriesResponse.get(categoryDTOS);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 수정", description = "[관리자] 카테고리 수정")
    @PutMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
        @PathVariable(name = "categoryId") final Long categoryId,
        @RequestParam(required = false) final String name,
        @RequestParam(required = false) final String description,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        final CategoryRequest request = new CategoryRequest(name, description);
        categoryService.update(categoryId, request, imageFile);
        final CategoryResponse response = CategoryResponse.update();

        return ResponseEntity.ok(response);
    }

    // 카테고리 삭제
    @Operation(summary = "카테고리 삭제", description = "[관리자] 카테고리 삭제")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> deleteCategory(
        @PathVariable(name = "categoryId") final Long categoryId
    ) {
        categoryService.delete(categoryId);
        final CategoryResponse response = CategoryResponse.delete();

        return ResponseEntity.ok(response);
    }
}
