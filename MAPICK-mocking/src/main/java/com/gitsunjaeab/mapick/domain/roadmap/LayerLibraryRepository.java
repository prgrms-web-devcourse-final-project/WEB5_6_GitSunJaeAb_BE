package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LayerLibraryRepository extends JpaRepository<LayerLibrary, Long> {

    LayerLibrary findFirstByMember(Member member);

    LayerLibrary findFirstByLayer(Layer layer);

}
