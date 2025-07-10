package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findFirstByRoadmap(Roadmap roadmap);

    Bookmark findFirstByMember(Member member);

}
