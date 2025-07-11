package com.gitsunjaeab.mapick.domain.category;

import com.gitsunjaeab.mapick.domain.roadmap.RoadmapCategoryRelation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByRoadmapCategoryRelations(RoadmapCategoryRelation roadmapCategoryRelation);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.roadmapCategoryRelations WHERE c.id = :id")
    Optional<Category> findCategoryWithRoadmapCategoryRelations(@Param("id") Long id);

}
