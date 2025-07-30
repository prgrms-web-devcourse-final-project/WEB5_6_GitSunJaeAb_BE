package com.gitsunjaeab.mapick.application.domain.member;

import com.gitsunjaeab.mapick.application.api.member.dto.request.MemberInterestRequest;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.category.Category;
import com.gitsunjaeab.mapick.application.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;


@Service
@RequiredArgsConstructor
public class MemberInterestService {

    private final MemberInterestRepository memberInterestRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;



    // 사용자 관심분야 생성
    @Transactional
    public void createMemberInterests(Long memberId, @Valid MemberInterestRequest memberInterestRequest) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        for (Long categoryId : memberInterestRequest.getCategoryId()) {

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CommonException(ResponseCode.INTEREST_NOT_FOUND));

            MemberInterest memberInterest = new MemberInterest();

            memberInterest.setCategory(category);
            memberInterest.setMember(member);
            memberInterest.setCreatedAt(OffsetDateTime.now());

            memberInterestRepository.save(memberInterest);

        }
    }

    // 사용자 관심분야 수정
    @Transactional
    public void updateMemberInterests(Long memberId, @Valid MemberInterestRequest memberInterestRequest) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        // 기존 관심 분야 삭제
        memberInterestRepository.deleteByMemberId(memberId);

        for (Long categoryId : memberInterestRequest.getCategoryId()) {

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CommonException(ResponseCode.INTEREST_NOT_FOUND));

            MemberInterest memberInterest = new MemberInterest();

            memberInterest.setCategory(category);
            memberInterest.setMember(member);
            memberInterest.setCreatedAt(OffsetDateTime.now());

            memberInterestRepository.save(memberInterest);

        }
    }
}
