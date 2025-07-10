package com.gitsunjaeab.mapick.category;

import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelation;
import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelationRepository;
import com.gitsunjaeab.mapick.member_interest.entity.MemberInterest;
import com.gitsunjaeab.mapick.member_interest.MemberInterestRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapCategoryRelationRepository mapCategoryRelationRepository;
    private final MemberInterestRepository memberInterestRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final MapCategoryRelationRepository mapCategoryRelationRepository,
            final MemberInterestRepository memberInterestRepository) {
        this.categoryRepository = categoryRepository;
        this.mapCategoryRelationRepository = mapCategoryRelationRepository;
        this.memberInterestRepository = memberInterestRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setCategoryImage(category.getCategoryImage());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setMapCategoryRelations(category.getMapCategoryRelations() == null ? null : category.getMapCategoryRelations().getId());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setCategoryImage(categoryDTO.getCategoryImage());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        final MapCategoryRelation mapCategoryRelations = categoryDTO.getMapCategoryRelations() == null ? null : mapCategoryRelationRepository.findById(categoryDTO.getMapCategoryRelations())
                .orElseThrow(() -> new NotFoundException("mapCategoryRelations not found"));
        category.setMapCategoryRelations(mapCategoryRelations);
        return category;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final MemberInterest interestMemberInterest = memberInterestRepository.findFirstByInterest(category);
        if (interestMemberInterest != null) {
            referencedWarning.setKey("category.memberInterest.interest.referenced");
            referencedWarning.addParam(interestMemberInterest.getId());
            return referencedWarning;
        }
        return null;
    }

}
