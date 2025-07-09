package com.gitsunjaeab.mapick.category;

import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByMapCategoryRelations(MapCategoryRelation mapCategoryRelation);

}
