package com.gitsunjaeab.mapick.application.member;

import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberInterestRepository;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.api.member.dto.MemberInterestDTO;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MemberInterestService {

    private final MemberInterestRepository memberInterestRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public MemberInterestService(final MemberInterestRepository memberInterestRepository,
            final CategoryRepository categoryRepository, final MemberRepository memberRepository) {
        this.memberInterestRepository = memberInterestRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    public List<MemberInterestDTO> findAll() {
        final List<MemberInterest> memberInterests = memberInterestRepository.findAll(Sort.by("id"));
        return memberInterests.stream()
                .map(memberInterest -> roadmapToDTO(memberInterest, new MemberInterestDTO()))
                .toList();
    }

    public MemberInterestDTO get(final Long id) {
        return memberInterestRepository.findById(id)
                .map(memberInterest -> roadmapToDTO(memberInterest, new MemberInterestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberInterestDTO memberInterestDTO) {
        final MemberInterest memberInterest = new MemberInterest();
        roadmapToEntity(memberInterestDTO, memberInterest);
        return memberInterestRepository.save(memberInterest).getId();
    }

    public void update(final Long id, final MemberInterestDTO memberInterestDTO) {
        final MemberInterest memberInterest = memberInterestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(memberInterestDTO, memberInterest);
        memberInterestRepository.save(memberInterest);
    }

    public void delete(final Long id) {
        memberInterestRepository.deleteById(id);
    }

    private MemberInterestDTO roadmapToDTO(final MemberInterest memberInterest,
            final MemberInterestDTO memberInterestDTO) {
        memberInterestDTO.setId(memberInterest.getId());
        memberInterestDTO.setCreatedAt(memberInterest.getCreatedAt());
        memberInterestDTO.setCategory(memberInterest.getCategory() == null ? null : memberInterest.getCategory().getId());
        memberInterestDTO.setMember(memberInterest.getMember() == null ? null : memberInterest.getMember().getId());
        return memberInterestDTO;
    }

    private MemberInterest roadmapToEntity(final MemberInterestDTO memberInterestDTO,
            final MemberInterest memberInterest) {
        memberInterest.setCreatedAt(memberInterestDTO.getCreatedAt());
        final Category interest = memberInterestDTO.getCategory() == null ? null : categoryRepository.findById(memberInterestDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("interest not found"));
        memberInterest.setCategory(interest);
        final Member member = memberInterestDTO.getMember() == null ? null : memberRepository.findById(memberInterestDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberInterest.setMember(member);
        return memberInterest;
    }

}
