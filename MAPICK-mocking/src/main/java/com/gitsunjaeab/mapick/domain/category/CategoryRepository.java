package com.gitsunjaeab.mapick.domain.category;

import com.gitsunjaeab.mapick.domain.roadmap.RoadmapCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByRoadmapCategoryRelations(RoadmapCategoryRelation roadmapCategoryRelation);

}
