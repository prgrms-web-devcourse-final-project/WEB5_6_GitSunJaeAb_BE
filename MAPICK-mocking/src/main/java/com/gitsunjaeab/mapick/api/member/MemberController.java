package com.gitsunjaeab.mapick.api.member;

import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.application.member.MemberInterestService;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;
    private final MemberInterestService memberInterestService;

    public MemberController(final MemberService memberService,
        final MemberInterestService memberInterestService) {
        this.memberService = memberService;
        this.memberInterestService = memberInterestService;
    }

//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createMember(@RequestBody @Valid final MemberDTO memberDTO) {
//        final Long createdId = memberService.create(memberDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }
//

    @GetMapping("/{membersId}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable(name = "membersId") final Long membersId) {
        return ResponseEntity.ok(memberService.get(membersId));
    }

    @PutMapping("/{membersId}")
    public ResponseEntity<Long> updateMember(@PathVariable(name = "membersId") final Long membersId,
            @RequestBody @Valid final MemberDTO memberDTO) {
        memberService.update(membersId, memberDTO);
        return ResponseEntity.ok(membersId);
    }

    @DeleteMapping("/{membersId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "membersId") final Long membersId) {
        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(membersId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        memberService.delete(membersId);
        return ResponseEntity.noContent().build();
    }

    // TODO 회원 관심분야 선택 (POST)
//    @PostMapping("/interests")
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createMemberInterest(
//        @RequestBody @Valid final MemberInterestDTO memberInterestDTO) {
//        final Long createdId = memberInterestService.create(memberInterestDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }

    // TODO 회원 관심분야 수정 (PUT)
//    @PutMapping("/{memberId}")
//    public ResponseEntity<Long> updateMemberInterest(@PathVariable(name = "memberInterestsId") final Long memberInterestsId,
//        @RequestBody @Valid final MemberInterestDTO memberInterestDTO) {
//        memberInterestService.update(memberInterestsId, memberInterestDTO);
//        return ResponseEntity.ok(memberInterestsId);
//    }


}
