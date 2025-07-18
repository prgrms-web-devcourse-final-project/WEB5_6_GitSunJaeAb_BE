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
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SupabaseStorageService supabaseStorageService;

    @Transactional
    public void create(CategoryRequest request, MultipartFile imageFile) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCreatedAt(OffsetDateTime.now());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            category.setCategoryImage(imageUrl);
        }
        categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by("id"));
    }

    @Transactional
    public Category update(Long id, CategoryRequest request, final MultipartFile imageFile) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다."));

        if (!(request.getName() == null))
            category.setName(request.getName());

        if (!(request.getDescription() == null))
            category.setDescription(request.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            category.setCategoryImage(imageUrl);
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(final Long id){
        categoryRepository.deleteById(id);
    }
}
