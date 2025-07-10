package com.gitsunjaeab.mapick.comment;

import com.gitsunjaeab.mapick.comment.entity.Comment;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findFirstByRoadmap(Roadmap map);

    Comment findFirstByMember(Member member);

}
