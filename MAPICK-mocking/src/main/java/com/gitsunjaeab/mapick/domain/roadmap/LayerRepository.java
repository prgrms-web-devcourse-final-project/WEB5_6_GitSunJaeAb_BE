package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LayerRepository extends JpaRepository<Layer, Long> {

    Layer findFirstByMember(Member member);

    Layer findFirstByRoadmap(Roadmap roadmap);

}
