package com.gitsunjaeab.mapick.domain.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapCategoryRelationRepository extends JpaRepository<RoadmapCategoryRelation, Long> {

    RoadmapCategoryRelation findFirstByRoadmap(Roadmap roadmap);

}
