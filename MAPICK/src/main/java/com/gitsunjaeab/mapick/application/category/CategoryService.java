package com.gitsunjaeab.mapick.application.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryDTO;
import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.common.EntityFinder;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final EntityFinder entityFinder;

    @Transactional
    public void create(final CategoryRequest request, final MultipartFile imageFile) {
        final Category category = new Category(request);

        if (imageFile != null && !imageFile.isEmpty()) {
            final String imageUrl = supabaseStorageService.upload(imageFile);
            category.setCategoryImage(imageUrl);
        }

        categoryRepository.save(category);
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll();

        return categories.stream()
            .map(CategoryDTO::new).toList();
    }

    public List<CategoryDTO> getTop5List() {
        final List<Category> categories = categoryRepository.findTop5Categories();

        return categories.stream().map(CategoryDTO::new)
            .toList();
    }

    @Transactional
    public Category update(final Long categoryId, final CategoryRequest request, final MultipartFile imageFile) {
        final Category category = entityFinder.findByCategoryId(categoryId);

        if (!(request.getName() == null))
            category.setName(request.getName());

        if (!(request.getDescription() == null))
            category.setDescription(request.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            final String imageUrl = supabaseStorageService.upload(imageFile);
            category.setCategoryImage(imageUrl);
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(final Long categoryId){
        categoryRepository.deleteById(categoryId);
    }
}
