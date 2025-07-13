package com.gitsunjaeab.mapick.domain.comment;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findFirstByRoadmap(Roadmap map);

    Comment findFirstByMember(Member member);

    List<Comment> findAllByRoadmap_Id(Long roadmapId);
}
