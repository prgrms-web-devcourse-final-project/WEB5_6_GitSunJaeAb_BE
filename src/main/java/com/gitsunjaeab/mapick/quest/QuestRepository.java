package com.gitsunjaeab.mapick.quest;

import com.gitsunjaeab.mapick.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestRepository extends JpaRepository<Quest, Long> {

    Quest findFirstByMember(Member member);

}
