package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MarkerRepository extends JpaRepository<Marker, Long> {

    Marker findFirstByMember(Member member);

    Marker findFirstByLayer(Layer layer);

}
