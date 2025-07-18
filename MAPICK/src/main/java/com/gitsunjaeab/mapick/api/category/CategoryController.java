package com.gitsunjaeab.mapick.api.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryListResponse;
import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.application.category.CategoryService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.category.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "카테고리 관리 API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 전체 카테고리 조회
    @GetMapping
    @Operation(summary = "전체 카테고리 조회", description = "[관리자/사용자용] 전체 카테고리 목록을 조회")
    public ResponseEntity<CategoryListResponse> getCategoryList() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(CategoryListResponse.of(categories));
    }

    // 카테고리 생성 (관리자)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "카테고리 생성", description = "[관리자] 카테고리 생성")
    public ResponseEntity<ApiResponse> createCategory(
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        CategoryRequest request = new CategoryRequest(name, description);
        categoryService.create(request, imageFile);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "카테고리 생성 완료"));
    }

    // 카테고리 수정 (관리자)
    @PutMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "카테고리 수정", description = "[관리자] 카테고리 수정")
    public ResponseEntity<ApiResponse> updateCategory(
        @PathVariable(name = "categoryId") Long categoryId,
        @Valid @RequestBody CategoryRequest request,
        MultipartFile imageFile
    ) {
//        categoryService.update(categoryId, request, imageFile);
//        Category updatedCategory = categoryService.findById(categoryId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "카테고리 수정 완료"));
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "[관리자] 카테고리 삭제")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable(name = "categoryId") final Long categoryId) {
        categoryService.delete(categoryId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "카테고리 삭제 완료"));
    }
}
