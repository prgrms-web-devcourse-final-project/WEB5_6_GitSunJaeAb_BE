package com.gitsunjaeab.mapick.map_editor;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MapEditorRepository extends JpaRepository<MapEditor, Long> {

    MapEditor findFirstByMap(Map map);

    MapEditor findFirstByMember(Member member);

    MapEditor findFirstByInvitedBy(Member member);

}
