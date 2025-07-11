package com.gitsunjaeab.mapick.application.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryDTO;
import com.gitsunjaeab.mapick.api.category.dto.CategoryListResponse;
import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.api.category.dto.CategoryResponse;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapCategoryRelationRepository;
import com.gitsunjaeab.mapick.domain.member.MemberInterestRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RoadmapCategoryRelationRepository roadmapCategoryRelationRepository;
    private final MemberInterestRepository memberInterestRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final RoadmapCategoryRelationRepository roadmapCategoryRelationRepository,
            final MemberInterestRepository memberInterestRepository) {
        this.categoryRepository = categoryRepository;
        this.roadmapCategoryRelationRepository = roadmapCategoryRelationRepository;
        this.memberInterestRepository = memberInterestRepository;
    }

    // 전체 조회
    public CategoryListResponse findAll() {
        final List<Category> categoryList = categoryRepository.findAll(Sort.by("id"));
        return CategoryListResponse.of(categoryList);
    }

    // 특정 조회
    public CategoryResponse findById(Long Id) {
        final Category category = categoryRepository.findById(Id)
            .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다. id="));
        return CategoryResponse.of(category);
    }

//    public CategoryDTO get(final Long id) {
//        return categoryRepository.findById(id)
//                .map(category -> roadmapToDTO(category, new CategoryDTO()))
//                .orElseThrow(NotFoundException::new);
//    }
//
    public Category create(final CategoryRequest request) {
        final Category category = new Category();
        roadmapToEntity(request, category);
        category.setCreatedAt(OffsetDateTime.now());
        return categoryRepository.save(category);
    }
//
    @Transactional
    public Category update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        roadmapToEntity(request, category);

        return categoryRepository.findCategoryWithRoadmapCategoryRelations(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }
    // 카테고리 삭제는 구현하지않음 (카테고리 삭제 시 연관된 로드맵 처리 문제 발생)
//    public void delete(final Long id) {
//        categoryRepository.deleteById(id);
//    }
//

//    private CategoryDTO roadmapToDTO(final Category category, final CategoryDTO categoryDTO) {
//        categoryDTO.setId(category.getId());
//        categoryDTO.setName(category.getName());
//        categoryDTO.setDescription(category.getDescription());
//        categoryDTO.setCategoryImage(category.getCategoryImage());
//        categoryDTO.setCreatedAt(category.getCreatedAt());
//        return categoryDTO;
//    }
//
    private Category roadmapToEntity(final CategoryRequest request, final Category category) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCategoryImage(request.getCategoryImage());

        if (request.getCreatedAt() != null) {
            category.setCreatedAt(request.getCreatedAt());
        }

        return category;
    }

//    public ReferencedWarning getReferencedWarning(final Long id) {
//        final ReferencedWarning referencedWarning = new ReferencedWarning();
//        final Category category = categoryRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        final MemberInterest interestMemberInterest = memberInterestRepository.findFirstByInterest(category);
//        if (interestMemberInterest != null) {
//            referencedWarning.setKey("category.memberInterest.interest.referenced");
//            referencedWarning.addParam(interestMemberInterest.getId());
//            return referencedWarning;
//        }
//        return null;
//    }

}
