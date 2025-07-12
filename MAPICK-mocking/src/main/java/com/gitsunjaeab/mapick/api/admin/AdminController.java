package com.gitsunjaeab.mapick.api.admin;

import com.gitsunjaeab.mapick.api.member.dto.MemberListResponse;
import com.gitsunjaeab.mapick.application.member.MemberService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private final MemberService memberService;

    public AdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 전체 회원 조회
    // NOTE 일반 사용자, 관리자, 블랙리스트 사용자
    @GetMapping("/members")
    public ResponseEntity<MemberListResponse> getAllMembers() {
        MemberListResponse response = memberService.findAll();
        return ResponseEntity.ok(response);
    }


}
