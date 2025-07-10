package com.gitsunjaeab.mapick.member.controller;

import com.gitsunjaeab.mapick.member.MemberService;
import com.gitsunjaeab.mapick.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createMember(@RequestBody @Valid final MemberDTO memberDTO) {
//        final Long createdId = memberService.create(memberDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }
//
    // TODO : ADMIN Controller 로 가야될듯 (전체회원조회, /admin/members/list)
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAll());
    }

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

}
