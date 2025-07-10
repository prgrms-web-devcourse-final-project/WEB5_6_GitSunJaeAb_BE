package com.gitsunjaeab.mapick.member_interest.controller;

import com.gitsunjaeab.mapick.category.Category;
import com.gitsunjaeab.mapick.category.CategoryRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.member_interest.dto.MemberInterestDTO;
import com.gitsunjaeab.mapick.member_interest.MemberInterestService;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/memberInterests", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberInterestController {

    private final MemberInterestService memberInterestService;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public MemberInterestController(final MemberInterestService memberInterestService,
            final CategoryRepository categoryRepository, final MemberRepository memberRepository) {
        this.memberInterestService = memberInterestService;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    // TODO : 해당경로가 명세서에 없는데 /categories 로 써야되는지..

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberInterest(
        @RequestBody @Valid final MemberInterestDTO memberInterestDTO) {
        final Long createdId = memberInterestService.create(memberInterestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<List<MemberInterestDTO>> getAllMemberInterests() {
//        return ResponseEntity.ok(memberInterestService.findAll());
//    }

    @GetMapping("/{memberInterestsId}")
    public ResponseEntity<MemberInterestDTO> getMemberInterest(
            @PathVariable(name = "memberInterestsId") final Long memberInterestsId) {
        return ResponseEntity.ok(memberInterestService.get(memberInterestsId));
    }

    @PutMapping("/{memberInterestsId}")
    public ResponseEntity<Long> updateMemberInterest(@PathVariable(name = "memberInterestsId") final Long memberInterestsId,
            @RequestBody @Valid final MemberInterestDTO memberInterestDTO) {
        memberInterestService.update(memberInterestsId, memberInterestDTO);
        return ResponseEntity.ok(memberInterestsId);
    }

    @DeleteMapping("/{memberInterestsId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberInterest(@PathVariable(name = "memberInterestsId") final Long memberInterestsId) {
        memberInterestService.delete(memberInterestsId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/interestValues")
//    public ResponseEntity<Map<Long, String>> getInterestValues() {
//        return ResponseEntity.ok(categoryRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Category::getId, Category::getName)));
//    }
//
//    @GetMapping("/memberValues")
//    public ResponseEntity<Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }

}
