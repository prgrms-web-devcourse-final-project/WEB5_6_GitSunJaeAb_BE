package com.gitsunjaeab.mapick.member;

import com.gitsunjaeab.mapick.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailIgnoreCase(String email);

}
