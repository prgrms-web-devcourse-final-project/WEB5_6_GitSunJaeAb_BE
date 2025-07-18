package com.gitsunjaeab.mapick.application.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
//    private final FileStorageService fileStorageService;
    private final SupabaseStorageService supabaseStorageService;

    // 전체 카테고리 조회
    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by("id"));
    }

    // 특정 카테고리 조회
    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
    }


    // 카테고리 생성
    public void create(CategoryRequest request, MultipartFile imageFile) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCreatedAt(OffsetDateTime.now());

        System.out.println("imageFile = " + imageFile);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            System.out.println("업로드 후 URL = " + imageUrl);
            category.setCategoryImage(imageUrl); // DB에 퍼블릭 URL 저장
        }
        categoryRepository.save(category);
    }

    // 카테고리 수정
    @Transactional
    public Category update(Long id, CategoryRequest request, final MultipartFile imageFile) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다. id=" + id));
        roadmapToEntity(request, category);

//        // 파일 업로드 처리
//        if (imageFile != null && !imageFile.isEmpty()) {
//            try {
//                // 기존 파일 삭제
//                if (category.getCategoryImage() != null) {
//                    fileStorageService.delete(category.getCategoryImage());
//                }
//                // 새 파일 업로드
//                String imageUrl = fileStorageService.upload(imageFile);
//                category.setCategoryImage(imageUrl);
//            } catch (Exception e) {
//                throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
//            }
//        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(final Long id){
        categoryRepository.deleteById(id);
    }

    private Category roadmapToEntity(final CategoryRequest request, final Category category) {
        // null이 아닌 경우에만 업데이트 (부분 업데이트 지원)
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        return category;
    }
}
