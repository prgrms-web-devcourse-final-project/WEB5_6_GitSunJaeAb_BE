package com.gitsunjaeab.mapick.member_interest;

import com.gitsunjaeab.mapick.category.Category;
import com.gitsunjaeab.mapick.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {

    MemberInterest findFirstByInterest(Category category);

    MemberInterest findFirstByMember(Member member);

}
