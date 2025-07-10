package com.gitsunjaeab.mapick.domain.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapHashtagRelationRepository extends JpaRepository<RoadmapHashtagRelation, Long> {

    RoadmapHashtagRelation findFirstByHashtag(Hashtag hashtag);

    RoadmapHashtagRelation findFirstByRoadmap(Roadmap map);

}
