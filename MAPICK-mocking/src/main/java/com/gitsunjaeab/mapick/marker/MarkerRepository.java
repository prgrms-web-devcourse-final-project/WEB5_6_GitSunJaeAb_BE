package com.gitsunjaeab.mapick.marker;

import com.gitsunjaeab.mapick.layer.Layer;
import com.gitsunjaeab.mapick.marker.entity.Marker;
import com.gitsunjaeab.mapick.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MarkerRepository extends JpaRepository<Marker, Long> {

    Marker findFirstByMember(Member member);

    Marker findFirstByLayer(Layer layer);

}
