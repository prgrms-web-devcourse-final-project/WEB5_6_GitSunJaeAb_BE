package com.gitsunjaeab.mapick.application.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryDTO;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapCategoryRelationRepository;
import com.gitsunjaeab.mapick.domain.member.MemberInterestRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
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

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
                .map(category -> roadmapToDTO(category, new CategoryDTO()))
                .toList();
    }

//    public CategoryDTO get(final Long id) {
//        return categoryRepository.findById(id)
//                .map(category -> roadmapToDTO(category, new CategoryDTO()))
//                .orElseThrow(NotFoundException::new);
//    }
//
    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        roadmapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }
//
    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }
//
//    public void delete(final Long id) {
//        categoryRepository.deleteById(id);
//    }
//
    private CategoryDTO roadmapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setCategoryImage(category.getCategoryImage());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        return categoryDTO;
    }
//
    private Category roadmapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setCategoryImage(categoryDTO.getCategoryImage());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        return category;
    }
//
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
