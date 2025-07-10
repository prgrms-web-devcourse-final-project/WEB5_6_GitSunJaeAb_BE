package com.gitsunjaeab.mapick.map_category_relation;

import com.gitsunjaeab.mapick.map.Map;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MapCategoryRelationRepository extends JpaRepository<MapCategoryRelation, Long> {

    MapCategoryRelation findFirstByMap(Map map);

}
