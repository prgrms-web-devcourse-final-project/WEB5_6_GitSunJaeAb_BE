package com.gitsunjaeab.mapick.domain.member;

import com.gitsunjaeab.mapick.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {

    MemberInterest findFirstByCategory(Category category);

    MemberInterest findFirstByMember(Member member);

}
