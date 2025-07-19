package com.gitsunjaeab.mapick.domain.member;

import com.gitsunjaeab.mapick.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {

    MemberInterest findFirstByCategory(Category category);

    MemberInterest findFirstByMember(Member member);

    void deleteByMemberId(Long memberId);

    List<MemberInterest> findAllByMemberId(Long memberId);
}
