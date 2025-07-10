package com.gitsunjaeab.mapick.roadmap_category_relation;

import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap_category_relation.entity.RoadmapCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapCategoryRelationRepository extends JpaRepository<RoadmapCategoryRelation, Long> {

    RoadmapCategoryRelation findFirstByRoadmap(Roadmap roadmap);

}
