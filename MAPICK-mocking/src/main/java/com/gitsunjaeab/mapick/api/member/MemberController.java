package com.gitsunjaeab.mapick.api.member;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberInterestDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberInterestRequest;
import com.gitsunjaeab.mapick.api.member.dto.MemberInterestResponse;
import com.gitsunjaeab.mapick.api.member.dto.MemberListResponse;
import com.gitsunjaeab.mapick.api.member.dto.MemberProfileResponse;
import com.gitsunjaeab.mapick.api.member.dto.MemberProfileUpdateRequest;
import com.gitsunjaeab.mapick.api.member.dto.MemberProfileUpdateResponse;
import com.gitsunjaeab.mapick.api.member.dto.MemberResponse;
import com.gitsunjaeab.mapick.api.member.dto.MemberUpdateRequest;
import com.gitsunjaeab.mapick.api.member.dto.PasswordUpdateRequest;
import com.gitsunjaeab.mapick.api.member.dto.PasswordVerifyRequest;
import com.gitsunjaeab.mapick.api.member.dto.SimpleMessageResponse;
import com.gitsunjaeab.mapick.application.member.MemberInterestService;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "회원 관리 API", description = "회원 관리 및 마이페이지 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final MemberInterestService memberInterestService;

    public MemberController(final MemberService memberService,
        final MemberInterestService memberInterestService) {
        this.memberService = memberService;
        this.memberInterestService = memberInterestService;
    }

    // ===== 관리자 전용 API (ADMIN 권한 필요) =====
    
    
    // 전체 회원 조회 (관리자 전용)
    @GetMapping
    @Operation(summary = "전체 회원 조회 (관리자)", description = "[관리자 전용] 관리자만 접근 가능한 전체 회원 목록 조회")
    public ResponseEntity<MemberListResponse> getAllMembers() {
        MemberListResponse response = memberService.findAll();
        return ResponseEntity.ok(response);
    }

    // 특정 회원 조회 (관리자 전용)
    @GetMapping("/{membersId}")
    @Operation(summary = "회원 조회 (관리자)", description = "[관리자 전용] 관리자만 접근 가능한 특정 회원 정보 조회")
    public ResponseEntity<MemberResponse> getMember(@PathVariable(name = "membersId") final Long membersId) {
        MemberDTO memberDTO = memberService.get(membersId);
        // TODO: MemberService를 수정하여 직접 Member 엔티티를 반환하도록 개선 필요
        Member member = memberService.getMemberProfile(membersId); 
        return ResponseEntity.ok(MemberResponse.of(member));
    }

    // 회원 정보 수정 (관리자 전용)
    @PutMapping("/{membersId}")
    @Operation(summary = "회원 정보 수정 (관리자)", description = "[관리자 전용] 관리자만 접근 가능한 회원 정보 수정")
    public ResponseEntity<com.gitsunjaeab.mapick.common.response.ApiResponse> updateMember(@PathVariable(name = "membersId") final Long membersId,
            @RequestBody @Valid final MemberUpdateRequest memberUpdateRequest) {
        // TODO: MemberService를 수정하여 MemberUpdateRequest를 직접 받도록 개선 필요
        MemberDTO memberDTO = convertToMemberDTO(memberUpdateRequest);
        memberService.update(membersId, memberDTO);
        return ResponseEntity.ok(com.gitsunjaeab.mapick.common.response.ApiResponse.of(com.gitsunjaeab.mapick.common.response.ResponseCode.OK, "회원 정보 수정 완료"));
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

    // 회원 삭제 (관리자 전용)
    @DeleteMapping("/{membersId}")
    @Operation(summary = "회원 삭제 (관리자)", description = "[관리자 전용] 관리자만 접근 가능한 회원 삭제")
    public ResponseEntity<com.gitsunjaeab.mapick.common.response.ApiResponse> deleteMember(@PathVariable(name = "membersId") final Long membersId) {
        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(membersId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        memberService.delete(membersId);
        return ResponseEntity.ok(com.gitsunjaeab.mapick.common.response.ApiResponse.of(com.gitsunjaeab.mapick.common.response.ResponseCode.OK, "회원 삭제 완료"));
    }

    // ===== 사용자 전용 API (본인만 접근 가능) =====

    // 마이페이지 - 회원 프로필 조회 (본인만)
    @GetMapping("/{memberId}/profile")
    @Operation(summary = "회원 프로필 조회", description = "[사용자 전용] 본인 또는 관리자만 접근 가능한 프로필 조회")
    public ResponseEntity<MemberProfileResponse> getMemberProfile(
            @Parameter(description = "회원 ID") @PathVariable Long memberId) {
        
        Member member = memberService.getMemberProfile(memberId);
        MemberProfileResponse response = MemberProfileResponse.of(member);
        
        return ResponseEntity.ok(response);
    }

    // 마이페이지 - 회원 정보 수정 (본인만)
    @PutMapping("/{memberId}/profile")
    @Operation(summary = "회원 정보 수정", description = "[사용자 전용] 본인만 접근 가능한 프로필 정보 수정")
    public ResponseEntity<MemberProfileUpdateResponse> updateMemberProfile(
            @Parameter(description = "회원 ID") @PathVariable Long memberId,
            @Valid @RequestBody MemberProfileUpdateRequest request) {
        
        Member updatedMember = memberService.updateMemberProfile(
            memberId, 
            request.getNickname(), 
            request.getProfileImage(),
            request.getIntro(),
            request.getPhone()
        );
        
        MemberProfileUpdateResponse response = MemberProfileUpdateResponse.of(updatedMember);
        return ResponseEntity.ok(response);
    }

    // 마이페이지 - 비밀번호 확인 (본인만)
    @PostMapping("/{memberId}/password/verify")
    @Operation(summary = "비밀번호 확인", description = "[사용자 전용] 본인만 접근 가능한 비밀번호 확인")
    public ResponseEntity<SimpleMessageResponse> verifyPassword(
            @Parameter(description = "회원 ID") @PathVariable Long memberId,
            @Valid @RequestBody PasswordVerifyRequest request) {
        
        boolean isValid = memberService.verifyPassword(memberId, request.getCurrentPassword());
        
        if (isValid) {
            return ResponseEntity.ok(SimpleMessageResponse.of("비밀번호가 일치합니다."));
        } else {
            return ResponseEntity.badRequest().body(SimpleMessageResponse.of("비밀번호가 일치하지 않습니다."));
        }
    }

    // 마이페이지 - 비밀번호 수정 (본인만)
    @PutMapping("/{memberId}/password")
    @Operation(summary = "비밀번호 수정", description = "[사용자 전용] 본인만 접근 가능한 비밀번호 변경")
    public ResponseEntity<SimpleMessageResponse> updatePassword(
            @Parameter(description = "회원 ID") @PathVariable Long memberId,
            @Valid @RequestBody PasswordUpdateRequest request) {
        
        memberService.updatePassword(memberId, request.getNewPassword());
        
        return ResponseEntity.ok(SimpleMessageResponse.of("비밀번호가 성공적으로 변경되었습니다."));
    }

    // 마이페이지 - 회원 탈퇴 (본인만)
    @DeleteMapping("/{memberId}/withdraw")
    @Operation(summary = "회원 탈퇴", description = "[사용자 전용] 본인만 접근 가능한 회원 탈퇴")
    public ResponseEntity<SimpleMessageResponse> withdrawMember(
            @Parameter(description = "회원 ID") @PathVariable Long memberId) {
        
        memberService.withdrawMember(memberId);
        
        return ResponseEntity.ok(SimpleMessageResponse.of("회원 탈퇴가 완료되었습니다."));
    }

    // ===== 회원 관심분야 관리 API =====

    // 회원 관심분야 선택 (본인만)
    @PostMapping("/interests")
    @Operation(summary = "회원 관심분야 선택", description = "[사용자 전용] 본인만 접근 가능한 관심분야 선택")
    public ResponseEntity<MemberInterestResponse> createMemberInterest(
            @Valid @RequestBody MemberInterestRequest request) {
        
        // Request를 DTO로 변환
        MemberInterestDTO memberInterestDTO = convertToMemberInterestDTO(request);
        
        // 관심분야 생성 후 엔티티 반환
        MemberInterest createdInterest = memberInterestService.createAndReturnEntity(memberInterestDTO);
        
        return ResponseEntity.status(201).body(MemberInterestResponse.ofCreate(createdInterest));
    }

    // 회원 관심분야 수정 (본인만)
    @PutMapping("/interests/{memberInterestId}")
    @Operation(summary = "회원 관심분야 수정", description = "[사용자 전용] 본인만 접근 가능한 관심분야 수정")
    public ResponseEntity<com.gitsunjaeab.mapick.common.response.ApiResponse> updateMemberInterest(
            @Parameter(description = "관심분야 ID") @PathVariable Long memberInterestId,
            @Valid @RequestBody MemberInterestRequest request) {
        
        // Request를 DTO로 변환
        MemberInterestDTO memberInterestDTO = convertToMemberInterestDTO(request);
        
        memberInterestService.update(memberInterestId, memberInterestDTO);
        
        return ResponseEntity.ok(com.gitsunjaeab.mapick.common.response.ApiResponse.of(com.gitsunjaeab.mapick.common.response.ResponseCode.OK, "관심분야 수정 완료"));
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
