package com.gitsunjaeab.mapick.api.member;

import com.gitsunjaeab.mapick.api.member.dto.request.MemberInterestRequest;
import com.gitsunjaeab.mapick.api.member.dto.request.MemberProfileUpdateRequest;
import com.gitsunjaeab.mapick.api.member.dto.request.PasswordRequest;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberListResponse;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberProfileResponse;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberResponse;
import com.gitsunjaeab.mapick.api.member.dto.response.SimpleMessageResponse;
import com.gitsunjaeab.mapick.application.member.MemberInterestService;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Tag(name = "회원 관리 API", description = "회원 관리 및 마이페이지 관련 API")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberInterestService memberInterestService;



    /**
     *
     * 관리자
     *
     */


    // 전체 회원 조회 (관리자 전용) -> 완성(예외처리 필요)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    @Operation(summary = "전체 회원 조회 (관리자)", description = "[관리자 전용] 관리자만 접근 가능한 전체 회원 목록 조회" )
    public ResponseEntity<MemberListResponse> getAllMembers() {

        // todo 관리자 만 해당 url 사용 할 수 있도록 시큐리티 컨피그에 추가 필요

        MemberListResponse response = memberService.findAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 특정 회원 상세 조회 (관리자) -> 완성(예외처리 필요)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{memberId}")
    @Operation(summary = "특정 회원 조회(괸라자) ", description = " 특정 회원 정보 조회")
    public ResponseEntity<MemberResponse> getMember(@PathVariable(name = "memberId") final Long memberId) {

        MemberResponse response = memberService.getMember(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 회원의 블랙리스트 여부 수정 (관리자 전용) -> 완성(예외처리 필요)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/blacklist/{memberId}")
    @Operation(summary = "블랙리스트 여부 변경 (관리자)", description = "[관리자 전용] 회원의 블랙 리스트 여부 수정")
    public ResponseEntity<ApiResponse> updateMemberBlackList(@PathVariable(name = "memberId") final Long memberId) {

        // todo 관리자 만 해당 url 사용 할 수 있도록 시큐리티 컨피그에 추가 필요

        memberService.setMemberBlackList(memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "블랙리스트 설정 완료"));
    }

    // 회원의 관리자로 설정 (관리자 전용) -> 완성(예외처리 필요)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{memberId}")
    @Operation(summary = "회원 role 변경 (관리자)", description = "[관리자 전용] 회원의 role 수정")
    public ResponseEntity<ApiResponse> updateMemberRole(@PathVariable(name = "memberId") final Long memberId) {

        // todo 관리자 만 해당 url 사용 할 수 있도록 시큐리티 컨피그에 추가 필요

        memberService.setMemberRoleAdmin(memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원의 role 수정 완료"));
    }

    // 회원 삭제 (관리자 전용) -> 완성(예외처리 필요)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{memberId}") // 실제로 delete 되지 않는데 delete 로 두어도 되는지 질문 예정
    @Operation(summary = "회원 삭제(관리자)", description = "회원 삭제")
    public ResponseEntity<ApiResponse> deleteMember(@PathVariable(name = "memberId") final Long memberId) {

        // todo 관리자 만 해당 url 사용 할 수 있도록 시큐리티 컨피그에 추가 필요

        memberService.deleteMember(memberId); // 소프트 딜리트

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원 삭제 완료"));
    }



    /**
     *
     * 사용자
     *
     */

    // 본인 회원 정보 조회 (프로필) -> 완성(예외처리 필요)
    @GetMapping
    @Operation(summary = "회원 정보 조회", description = "[사용자 전용] 본인만 접근 가능한 프로필 조회" )
    public ResponseEntity<MemberProfileResponse> getMemberProfile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        MemberProfileResponse response = memberService.getMemberProfile(memberId);

        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정 (프로필) -> 완성(예외처리 필요)
    @PutMapping
    @Operation(summary = "회원 정보 수정(프로필)", description = "사용자 회원 정보 수정")
    public ResponseEntity<ApiResponse> updateMember(@RequestBody @Valid final MemberProfileUpdateRequest MemberProfileUpdateRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        memberService.updateMemberProfile(memberId, MemberProfileUpdateRequest);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원 정보 수정 완료"));
    }

    // 회원 탈퇴 (사용자) -> 완성(예외처리 필요)
    @DeleteMapping ("/withdraw")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    public ResponseEntity<ApiResponse> withdrawMember() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        memberService.deleteMember(memberId); // 소프트 딜리트

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원 탈퇴 완료"));
    }

    // ===== 회원 관심분야 관리 API =====

    // 회원 관심분야 선택 (본인만) -> 완성(예외처리 필요)
    @PostMapping("/interests")
    @Operation(summary = "회원 관심분야 선택", description = "[사용자 전용] 본인만 접근 가능한 관심분야 선택")
    public ResponseEntity<ApiResponse> createMemberInterest(@Valid @RequestBody MemberInterestRequest memberInterestRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        memberInterestService.createMemberInterests(memberId,memberInterestRequest);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "관심분야 선택 완료"));
    }

    // 회원 관심분야 수정 (본인만) -> 완성(예외처리 필요)
    @PutMapping("/interests")
    @Operation(summary = "회원 관심분야 수정", description = "[사용자 전용] 본인만 접근 가능한 관심분야 수정")
    public ResponseEntity<ApiResponse> updateMemberInterest(@Valid @RequestBody MemberInterestRequest memberInterestRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        memberInterestService.updateMemberInterests(memberId,memberInterestRequest);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "관심분야 수정 완료"));
    }

    // ===== 회원 비밀번호 관리 API =====

    // 마이페이지 - 비밀번호 확인 (본인만) -> 완성(예외처리 필요)
    @PostMapping("/password/verify")
    @Operation(summary = "비밀번호 확인", description = "[사용자 전용] 본인만 접근 가능한 비밀번호 확인")
    public ResponseEntity<ApiResponse> verifyPassword(@Valid @RequestBody PasswordRequest passwordRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        memberService.verifyPassword(memberId, passwordRequest.getPassword());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(ResponseCode.VERITY_PASSWORD_SUCCESS));
    }



}
