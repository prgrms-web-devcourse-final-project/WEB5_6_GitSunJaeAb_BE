package com.gitsunjaeab.mapick.application.member;

import com.gitsunjaeab.mapick.api.auth.dto.internal.SocialUserInfo;
import com.gitsunjaeab.mapick.api.auth.dto.request.SignupRequest;
import com.gitsunjaeab.mapick.api.member.dto.*;
import com.gitsunjaeab.mapick.api.member.dto.request.MemberProfileUpdateRequest;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.LoginType;
import com.gitsunjaeab.mapick.domain.auth.Role;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import com.gitsunjaeab.mapick.domain.member.MemberInterestRepository;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberInterestRepository memberInterestRepository;
    private final SupabaseStorageService supabaseStorageService;

    // 소셜 로그인 시 임시 닉네임 부여
    public String generateUniqueSocialNickname(String provider) {
        String nickname;
        do {
            int randomNumber = (int) (Math.random() * 1_000_000);
            nickname = provider + "_" + String.format("%06d", randomNumber);
        } while (memberRepository.existsByNickname(nickname)); // 중복 체크

        return nickname;
    }

    // 소셜 로그인 회원 가입
    public Member registerNewSocialMember(SocialUserInfo userInfo, String provider) {

        String nickname = generateUniqueSocialNickname(provider);

        Member member = Member.builder()
            .isBlacklisted(false)
            .name(userInfo.getName())
            .nickname(nickname)
            .email(userInfo.getEmail())
            .password(UUID.randomUUID().toString())
            .loginType(LoginType.SOCIAL)
            .provider(provider)
            .role(String.valueOf(Role.ROLE_USER))
            .status("ACTIVE")
            .profileImage(userInfo.getPicture())
            .loginCount(0L)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();

        Member savedMember;

        try{
            savedMember = memberRepository.save(member);
        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }

        return savedMember;
    }

    // 자체 로그인 회원 가입
    public void signup(SignupRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CommonException(ResponseCode.ALREADY_REGISTERED_EMAIL);
        }

        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new CommonException(ResponseCode.NICKNAME_DUPLICATED);
        }

        Member member = Member.builder()
            .isBlacklisted(false)
            .name(request.getName())
            .nickname(request.getNickname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .loginType(LoginType.LOCAL)
            .provider(null)
            .role(String.valueOf(Role.ROLE_USER))
            .status("ACTIVE")
            .profileImage(null)
            .loginCount(0L)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();

        try{
            memberRepository.save(member);
        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }
    }

    // 멤버 리스트 조회 - 관리자
    // complete
    public List<MemberListDTO> getAllMembers() {

        final List<Member> members = memberRepository.findAllByDeletedAtIsNull(Sort.by("id")); // 탈퇴하지 않은 사용자들만 죠회

        List<MemberListDTO> memberListDTOs = members.stream()
                .map(MemberListDTO::of)
                .toList();

        return memberListDTOs;
    }

    // 멤버 상세 조회(상세 버전) - 관리자
    // complete
    public MemberDTO getMember(final Long memberId) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        MemberDTO memberDTO = MemberDTO.of(member);

        return memberDTO;
    }

    // 사용자 정보 조회 - 사용자
    // complete
    @Transactional
    public MemberDetailDTO getMemberProfile(final Long memberId) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        List<MemberInterest> memberInterests = memberInterestRepository.findAllByMemberId(memberId);

        List<MemberInterestDTO> memberInterestDTOList = memberInterests.stream()
                .map(MemberInterestDTO::of)
                .toList();

        MemberDetailDTO memberDetailDto = MemberDetailDTO.of(member, memberInterestDTOList);

        return memberDetailDto;
    }

    // 사용자 정보(프로필) 수정
    // complete
    @Transactional
    public void updateMemberProfile(final Long memberId,
                                    final MemberProfileUpdateRequest memberProfileUpdateRequest,
                                    MultipartFile imageFile) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        String nickname = memberProfileUpdateRequest.getNickname();


        if (nickname != null && !nickname.isBlank() && !nickname.equals(member.getNickname())) {
            if(memberRepository.existsByNickname(nickname)){
                throw new CommonException(ResponseCode.NICKNAME_DUPLICATED);
            }
            member.setNickname(nickname);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = supabaseStorageService.upload(imageFile);
                member.setProfileImage(imageUrl);
            } catch (RuntimeException e) {
                throw new CommonException(ResponseCode.FILE_UPLOAD_FAILED);
            }
        }

        member.setUpdatedAt(OffsetDateTime.now());

    }

    // 관리자 - 특정 유저 블랙 리스트 설정
    // complete
    @Transactional
    public void setMemberBlackList(Long memberId) {

        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        // 회원 블랙 리스트 인지 확인
        if (member.getIsBlacklisted()){
            throw new CommonException(ResponseCode.ALREADY_REGISTERED_BLACKLIST);
        }
            member.setIsBlacklisted(true);

    }

    // 관리자 - 특정 유저 블랙 리스트 해제
    // complete
    @Transactional
    public void clearMemberBlackList(Long memberId) {

            Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                    .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

            // 회원 블랙 리스트가 아닌지 확인
            if (!member.getIsBlacklisted()){
                throw new CommonException(ResponseCode.ALREADY_NOT_REGISTERED_BLACKLIST);
            }

            member.setIsBlacklisted(false);

    }

    // 관리자 - 유저 관리자 권한 부여
    // complete
    @Transactional
    public void setMemberRoleAdmin(Long memberId) {

        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        if ("ROLE_ADMIN".equals(member.getRole())) { // 리터럴을 앞에 두어 null 방지
            throw new CommonException(ResponseCode.ALREADY_REGISTERED_ADMIN);
        }

        member.setRole("ROLE_ADMIN");

    }

    // 관리자 - 유저 관리자 권한 회수
    // complete
    @Transactional
    public void clearMemberRoleAdmin(Long memberId) {

            Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                    .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

            if ("ROLE_USER".equals(member.getRole())) { // 리터럴을 앞에 두어 null 방지
                throw new CommonException(ResponseCode.ALREADY_REGISTERED_USER);
            }

            member.setRole("ROLE_USER");

    }

    // 회원 삭제/탈퇴(관리자/사용자) - 소프트 딜리트
    // complete
    @Transactional
    public void deleteMember(final Long memberId) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        if (member.getDeletedAt() != null || member.getStatus().equals("WITHDRAWN")) {
            throw new CommonException(ResponseCode.ALREADY_DELETED_USER);
        }

        member.setStatus("WITHDRAWN");
        member.setDeletedAt(OffsetDateTime.now()); // 삭제 날짜에 현재 시간 입력
    }

    // 비밀번호 검증
    // complete
    public boolean verifyPassword(Long memberId, String password) {

        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CommonException(ResponseCode.INVALID_PASSWORD);
        }

        return true;
    }



}
