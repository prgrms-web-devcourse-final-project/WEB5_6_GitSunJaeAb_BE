package com.gitsunjaeab.mapick.application.member;

import com.gitsunjaeab.mapick.api.member.dto.request.MemberInterestRequest;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberInterestRepository;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.api.member.dto.MemberInterestDTO;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.util.NotFoundException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberInterestService {

    private final MemberInterestRepository memberInterestRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;



    public List<MemberInterestDTO> findAll() {
        final List<MemberInterest> memberInterests = memberInterestRepository.findAll(Sort.by("id"));
        return memberInterests.stream()
                .map(memberInterest -> convertToDTO(memberInterest, new MemberInterestDTO()))
                .toList();
    }

    public MemberInterestDTO get(final Long id) {
        return memberInterestRepository.findById(id)
                .map(memberInterest -> convertToDTO(memberInterest, new MemberInterestDTO()))
                .orElseThrow(NotFoundException::new);
    }

//    public Long create(final MemberInterestDTO memberInterestDTO) {
//        final MemberInterest memberInterest = new MemberInterest();
//        roadmapToEntity(memberInterestDTO, memberInterest);
//        return memberInterestRepository.save(memberInterest).getId();
//    }

    // 마이페이지 - 관심분야 생성 후 엔티티 반환
    private MemberInterestDTO convertToDTO(final MemberInterest memberInterest,
        final MemberInterestDTO dto) {
        dto.setId(memberInterest.getId());
        dto.setCreatedAt(memberInterest.getCreatedAt());

        dto.setCategory(
            memberInterest.getCategory() == null
                ? null
                : List.of(memberInterest.getCategory().getId()) // ✅ 단일값을 List로 감쌈
        );

        dto.setMember(
            memberInterest.getMember() == null
                ? null
                : memberInterest.getMember().getId()
        );

        return dto;
    }

    // 마이페이지 - 관심분야 수정 후 엔티티 반환
    @Transactional
    public void updateMemberInterests(MemberInterestDTO dto) {
        Long memberId = dto.getMember();

        // 1. 기존 관심분야 삭제
        memberInterestRepository.deleteByMemberId(memberId);

        // 2. 새로 생성된 관심분야들 저장
        List<MemberInterest> newInterests = roadmapToEntities(dto);
        memberInterestRepository.saveAll(newInterests);
    }

    public void delete(final Long id) {
        memberInterestRepository.deleteById(id);
    }

    public MemberInterestDTO convertToDTO(List<MemberInterest> interests) {
        MemberInterestDTO dto = new MemberInterestDTO();
        if (!interests.isEmpty()) {
            dto.setMember(interests.get(0).getMember().getId());
            dto.setCreatedAt(interests.get(0).getCreatedAt());
            dto.setId(null); // 여러 개니까 대표 ID 없을 수도 있음
        }
        dto.setCategory(
            interests.stream()
                .map(i -> i.getCategory().getId())
                .collect(Collectors.toList())
        );
        return dto;
    }

    public List<MemberInterest> roadmapToEntities(MemberInterestDTO dto) {
        List<MemberInterest> result = new ArrayList<>();

        if (dto.getCategory() == null || dto.getCategory().isEmpty()) {
            return result;
        }

        Member member = memberRepository.findById(dto.getMember())
            .orElseThrow(() -> new NotFoundException("member not found"));

        for (Long categoryId : dto.getCategory()) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("interest not found"));

            MemberInterest entity = new MemberInterest();
            entity.setCreatedAt(dto.getCreatedAt());
            entity.setMember(member);
            entity.setCategory(category);
            result.add(entity);
        }

        return result;
    }

    // 사용자 관심분야 생성
    @Transactional
    public void createMemberInterests(Long memberId, @Valid MemberInterestRequest memberInterestRequest) {

        final Member member = memberRepository.findById(memberId)
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

        final Member member = memberRepository.findById(memberId)
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
