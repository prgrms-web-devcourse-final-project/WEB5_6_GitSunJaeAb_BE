package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuestRepository extends JpaRepository<Quest, Long> {

    Quest findFirstByMember(Member member);


    @EntityGraph(attributePaths = "member")
    Optional<Quest> findWithMemberById(Long id);


    // 전체 조회용: member까지 즉시 로딩하는 JPQL 쿼리
    @Query("SELECT q FROM Quest q JOIN FETCH q.member ORDER BY q.id ASC")
    List<Quest> findAllWithMember();
}
