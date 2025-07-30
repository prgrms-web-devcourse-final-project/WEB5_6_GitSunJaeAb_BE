package com.gitsunjaeab.mapick.application.domain.comment;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findFirstByRoadmap(Roadmap map);

    Comment findFirstByMember(Member member);

    List<Comment> findAllByRoadmap_Id(Long roadmapId);

    List<Comment> findAllByQuest_Id(Long questId);

    List<Comment> findAllByMember_Id(Long memberId);

    Long countByMemberId(Long memberId);
}
