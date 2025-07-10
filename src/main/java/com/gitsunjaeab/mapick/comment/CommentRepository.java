package com.gitsunjaeab.mapick.comment;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findFirstByMap(Map map);

    Comment findFirstByMember(Member member);

}
