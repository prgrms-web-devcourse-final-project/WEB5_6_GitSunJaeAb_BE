package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestRepository extends JpaRepository<Quest, Long> {

    Quest findFirstByMember(Member member);

}
