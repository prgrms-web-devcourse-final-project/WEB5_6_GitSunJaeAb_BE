package com.gitsunjaeab.mapick.member_interest;

import com.gitsunjaeab.mapick.category.Category;
import com.gitsunjaeab.mapick.category.CategoryRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.member_interest.dto.MemberInterestDTO;
import com.gitsunjaeab.mapick.member_interest.entity.MemberInterest;
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
                .map(memberInterest -> mapToDTO(memberInterest, new MemberInterestDTO()))
                .toList();
    }

    public MemberInterestDTO get(final Long id) {
        return memberInterestRepository.findById(id)
                .map(memberInterest -> mapToDTO(memberInterest, new MemberInterestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberInterestDTO memberInterestDTO) {
        final MemberInterest memberInterest = new MemberInterest();
        mapToEntity(memberInterestDTO, memberInterest);
        return memberInterestRepository.save(memberInterest).getId();
    }

    public void update(final Long id, final MemberInterestDTO memberInterestDTO) {
        final MemberInterest memberInterest = memberInterestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberInterestDTO, memberInterest);
        memberInterestRepository.save(memberInterest);
    }

    public void delete(final Long id) {
        memberInterestRepository.deleteById(id);
    }

    private MemberInterestDTO mapToDTO(final MemberInterest memberInterest,
            final MemberInterestDTO memberInterestDTO) {
        memberInterestDTO.setId(memberInterest.getId());
        memberInterestDTO.setCreatedAt(memberInterest.getCreatedAt());
        memberInterestDTO.setInterest(memberInterest.getInterest() == null ? null : memberInterest.getInterest().getId());
        memberInterestDTO.setMember(memberInterest.getMember() == null ? null : memberInterest.getMember().getId());
        return memberInterestDTO;
    }

    private MemberInterest mapToEntity(final MemberInterestDTO memberInterestDTO,
            final MemberInterest memberInterest) {
        memberInterest.setCreatedAt(memberInterestDTO.getCreatedAt());
        final Category interest = memberInterestDTO.getInterest() == null ? null : categoryRepository.findById(memberInterestDTO.getInterest())
                .orElseThrow(() -> new NotFoundException("interest not found"));
        memberInterest.setInterest(interest);
        final Member member = memberInterestDTO.getMember() == null ? null : memberRepository.findById(memberInterestDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberInterest.setMember(member);
        return memberInterest;
    }

}
