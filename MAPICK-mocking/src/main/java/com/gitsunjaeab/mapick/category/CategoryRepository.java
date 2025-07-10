package com.gitsunjaeab.mapick.category;

import com.gitsunjaeab.mapick.category.entity.Category;
import com.gitsunjaeab.mapick.roadmap_category_relation.entity.RoadmapCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByRoadmapCategoryRelations(RoadmapCategoryRelation roadmapCategoryRelation);

}
