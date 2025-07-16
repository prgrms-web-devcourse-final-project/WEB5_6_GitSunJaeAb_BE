package com.gitsunjaeab.mapick.api.member;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberInterestDTO;
import com.gitsunjaeab.mapick.api.member.dto.request.MemberInterestRequest;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberListResponse;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberProfileResponse;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberResponse;
import com.gitsunjaeab.mapick.api.member.dto.request.MemberUpdateRequest;
import com.gitsunjaeab.mapick.api.member.dto.request.PasswordUpdateRequest;
import com.gitsunjaeab.mapick.api.member.dto.request.PasswordVerifyRequest;
import com.gitsunjaeab.mapick.api.member.dto.response.SimpleMessageResponse;
import com.gitsunjaeab.mapick.application.member.MemberInterestService;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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



    // 특정 회원 조회 (관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{memberId}")
    @Operation(summary = "특정 회원 조회 ", description = " 특정 회원 정보 조회")
    public ResponseEntity<MemberResponse> getMember(@PathVariable(name = "memberId") final Long memberId) {
        MemberResponse response = memberService.get(memberId);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 마이페이지 - 회원 프로필 조회 (본인만)
    @GetMapping
    @Operation(summary = "회원 프로필 조회", description = "[사용자 전용] 본인만 접근 가능한 프로필 조회" )
    public ResponseEntity<MemberProfileResponse> getMemberProfile() {
        Long memberId = 1L;


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("현재 로그인한 사용자: " + auth.getName());
        System.out.println("권한 목록: " + auth.getAuthorities());
        System.out.println("Principal 정보: " + auth.getPrincipal());

        Member member = memberService.getMemberProfile(memberId);
        MemberProfileResponse response = MemberProfileResponse.of(member);

        return ResponseEntity.ok(response);
    }

    // 전체 회원 조회 (관리자 전용) -> 완성
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    @Operation(summary = "전체 회원 조회 (관리자)", description = "[관리자 전용] 관리자만 접근 가능한 전체 회원 목록 조회" )
    public ResponseEntity<MemberListResponse> getAllMembers() {

        MemberListResponse response = memberService.findAll();

        return ResponseEntity.ok(response);
    }

    // 회원의 블랙리스트 여부 수정 (관리자 전용) -> 완성
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/blacklist/{memberId}")
    @Operation(summary = "블랙리스트 여부 변경 (관리자)", description = "[관리자 전용] 회원의 블랙 리스트 여부 수정")
    public ResponseEntity<ApiResponse> updateMemberBlackList(@PathVariable(name = "memberId") final Long memberId) {

        // todo 관리자 만 해당 url 사용 할 수 있도록 시큐리티 컨피그에 추가 필요

        memberService.setMemberBlackList(memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "블랙리스트 설정 완료"));
    }

    // 회원의 관리자로 설정 (관리자 전용) -> 완성
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{memberId}")
    @Operation(summary = "회원 role 변경 (관리자)", description = "[관리자 전용] 회원의 role 수정")
    public ResponseEntity<ApiResponse> updateMemberRole(@PathVariable(name = "memberId") final Long memberId) {

        // todo 관리자 만 해당 url 사용 할 수 있도록 시큐리티 컨피그에 추가 필요

        memberService.setMemberRoleAdmin(memberId);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원의 role 수정 완료"));
    }
    

    // 회원 정보 수정 (관리자 전용)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Operation(summary = "회원 정보 수정 (관리자)", description = "[관리자 전용] 관리자만 접근 가능한 회원 정보 수정")
    public ResponseEntity<ApiResponse> updateMember(final Long memberId, @RequestBody @Valid final MemberUpdateRequest memberUpdateRequest) {
        // TODO: MemberService를 수정하여 MemberUpdateRequest를 직접 받도록 개선 필요
//        MemberDTO memberDTO = convertToMemberDTO(memberUpdateRequest);
//        memberService.update(membersId, memberDTO);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원 정보 수정 완료"));
    }

    // 임시 변환 메서드 (TODO: 서비스 레이어 개선 후 제거)
    private MemberDTO convertToMemberDTO(MemberUpdateRequest request) {
        MemberDTO dto = new MemberDTO();
        dto.setBlacklisted(request.isBlacklisted());
        dto.setName(request.getName());
        dto.setNickname(request.getNickname());
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setLoginType(request.getLoginType());
        dto.setProvider(request.getProvider());
        dto.setRole(request.getRole());
        dto.setStatus(request.getStatus());
        dto.setProfileImage(request.getProfileImage());
        return dto;
    }

    // 회원 삭제 (관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping // 실제로 delete 되지 않는데 delete 로 두어도 되는지 질문 예정
    @Operation(summary = "회원 삭제", description = "회원 삭제")
    public ResponseEntity<ApiResponse> deleteMember(final Long memberId) {

        // todo 관리자 만 해당 url 사용 할 수 있도록 시큐리티 컨피그에 추가 필요

        memberService.deleteMember(memberId); // 소프트 딜리트

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원 삭제 완료"));
    }

    // 회원 삭제 (사용자)

    @DeleteMapping ("/withdraw")// 실제로 delete 되지 않는데 delete 로 두어도 되는지 질문 예정
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    public ResponseEntity<ApiResponse> withdrawMember(final Long memberId) {

        // todo 본인 객체 꺼내서 memberId 꺼내도록 변경

        memberService.deleteMember(memberId); // 소프트 딜리트

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "회원 탈퇴 완료"));
    }



    // 마이페이지 - 비밀번호 확인 (본인만)
    @PostMapping("/password/verify")
    @Operation(summary = "비밀번호 확인", description = "[사용자 전용] 본인만 접근 가능한 비밀번호 확인")
    public ResponseEntity<SimpleMessageResponse> verifyPassword(
            @Valid @RequestBody PasswordVerifyRequest request) {

        Long memberId = 1L;

        boolean isValid = memberService.verifyPassword(memberId, request.getCurrentPassword());

        if (isValid) {
            return ResponseEntity.ok(SimpleMessageResponse.of("비밀번호가 일치합니다."));
        } else {
            return ResponseEntity.badRequest().body(SimpleMessageResponse.of("비밀번호가 일치하지 않습니다."));
        }
    }

    // 마이페이지 - 비밀번호 수정 (본인만)
    @PutMapping("/password")
    @Operation(summary = "비밀번호 수정", description = "[사용자 전용] 본인만 접근 가능한 비밀번호 변경")
    public ResponseEntity<SimpleMessageResponse> updatePassword(
            @Valid @RequestBody PasswordUpdateRequest request) {

        Long memberId = 1L;
        
        memberService.updatePassword(memberId, request.getNewPassword());
        
        return ResponseEntity.ok(SimpleMessageResponse.of("비밀번호가 성공적으로 변경되었습니다."));
    }


    // ===== 회원 관심분야 관리 API =====

    // 회원 관심분야 선택 (본인만)
    @PostMapping("/interests")
    @Operation(summary = "회원 관심분야 선택", description = "[사용자 전용] 본인만 접근 가능한 관심분야 선택")
    public ResponseEntity<ApiResponse> createMemberInterest(
        @Valid @RequestBody MemberInterestRequest request) {

//        memberInterestService.createMemberInterests(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "관심분야 선택 완료"));
    }


    // 회원 관심분야 수정 (본인만)
    @PutMapping("/interests/{memberInterestId}")
    @Operation(summary = "회원 관심분야 수정", description = "[사용자 전용] 본인만 접근 가능한 관심분야 수정")
    public ResponseEntity<ApiResponse> updateMemberInterest(
            @Parameter(description = "관심분야 ID") @PathVariable Long memberInterestId,
            @Valid @RequestBody MemberInterestRequest request) {
        
        // Request를 DTO로 변환
//        MemberInterestDTO memberInterestDTO = convertToMemberInterestDTO(request);
//
//        memberInterestService.update(memberInterestId, memberInterestDTO);
        
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "관심분야 수정 완료"));
    }

    // Request를 DTO로 변환하는 임시 메서드
    private MemberInterestDTO convertToMemberInterestDTO(MemberInterestRequest request) {
        MemberInterestDTO dto = new MemberInterestDTO();
        dto.setCategory(request.getCategoryId());
        dto.setMember(request.getMemberId());
        dto.setCreatedAt(java.time.OffsetDateTime.now()); // 현재 시간으로 설정
        return dto;
    }

}
