package com.gitsunjaeab.mapick.member_interest;

import com.gitsunjaeab.mapick.category.entity.Category;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member_interest.entity.MemberInterest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {

    MemberInterest findFirstByInterest(Category category);

    MemberInterest findFirstByMember(Member member);

}
