package com.gitsunjaeab.mapick.bookmark;

import com.gitsunjaeab.mapick.bookmark.entity.Bookmark;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findFirstByRoadmap(Roadmap map);

    Bookmark findFirstByMember(Member member);

}
