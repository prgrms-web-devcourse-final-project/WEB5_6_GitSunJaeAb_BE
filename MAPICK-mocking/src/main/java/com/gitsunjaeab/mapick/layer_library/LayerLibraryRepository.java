package com.gitsunjaeab.mapick.layer_library;

import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.layer_library.entity.LayerLibrary;
import com.gitsunjaeab.mapick.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LayerLibraryRepository extends JpaRepository<LayerLibrary, Long> {

    LayerLibrary findFirstByMember(Member member);

    LayerLibrary findFirstByLayer(Layer layer);

}
