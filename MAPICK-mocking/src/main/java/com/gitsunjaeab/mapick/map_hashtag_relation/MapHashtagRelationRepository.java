package com.gitsunjaeab.mapick.map_hashtag_relation;

import com.gitsunjaeab.mapick.hashtag.Hashtag;
import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.map_hashtag_relation.entity.MapHashtagRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MapHashtagRelationRepository extends JpaRepository<MapHashtagRelation, Long> {

    MapHashtagRelation findFirstByHashtag(Hashtag hashtag);

    MapHashtagRelation findFirstByMap(Map map);

}
