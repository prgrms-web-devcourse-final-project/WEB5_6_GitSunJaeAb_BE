package com.gitsunjaeab.mapick.layer;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LayerRepository extends JpaRepository<Layer, Long> {

    Layer findFirstByMember(Member member);

    Layer findFirstByMap(Map map);

}
