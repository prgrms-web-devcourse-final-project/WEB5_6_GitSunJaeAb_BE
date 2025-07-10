package com.gitsunjaeab.mapick.roadmap_hashtag_relation;

import com.gitsunjaeab.mapick.hashtag.entity.Hashtag;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.entity.RoadmapHashtagRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoadmapHashtagRelationRepository extends JpaRepository<RoadmapHashtagRelation, Long> {

    RoadmapHashtagRelation findFirstByHashtag(Hashtag hashtag);

    RoadmapHashtagRelation findFirstByRoadmap(Roadmap map);

}
