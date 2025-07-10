package com.gitsunjaeab.mapick.layer;

import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LayerRepository extends JpaRepository<Layer, Long> {

    Layer findFirstByMember(Member member);

    Layer findFirstByRoadmap(Roadmap roadmap);

}
