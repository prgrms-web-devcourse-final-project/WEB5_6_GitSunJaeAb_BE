package com.gitsunjaeab.mapick.bookmark;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findFirstByMap(Map map);

    Bookmark findFirstByMember(Member member);

}
