package com.gitsunjaeab.mapick.map;

import com.gitsunjaeab.mapick.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MapRepository extends JpaRepository<Map, Long> {

    Map findFirstByMember(Member member);

    Map findFirstByOriginalMapAndIdNot(Map map, final Long id);

}
