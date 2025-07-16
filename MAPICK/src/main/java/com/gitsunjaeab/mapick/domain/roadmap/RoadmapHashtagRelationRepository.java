package com.gitsunjaeab.mapick.domain.roadmap;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RoadmapHashtagRelationRepository extends JpaRepository<RoadmapHashtagRelation, Long> {

    RoadmapHashtagRelation findFirstByHashtag(Hashtag hashtag);

    RoadmapHashtagRelation findFirstByRoadmap(Roadmap map);

    // 지도에 적용된 해시태그들의 Id 조회
    @Query("SELECT rh.hashtag.id FROM RoadmapHashtagRelation rh WHERE rh.roadmap.id = :roadmapId")
    List<Long> findAllByRoadmap_Id(Long roadmapId);
}
