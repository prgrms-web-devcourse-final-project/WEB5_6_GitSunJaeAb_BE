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

    // 멤버 리스트 조회
    public List<MemberListDTO> findAll() {

        final List<Member> members = memberRepository.findAllByDeletedAtIsNull(Sort.by("id"));

        List<MemberListDTO> memberListDTOs = members.stream()
                .map(MemberListDTO::of).toList();

        return memberListDTOs;
    }

    // 멤버 상세 조회(상세 버전) - 관리자
    public MemberDTO getMember(final Long memberId) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        MemberDTO memberDTO = MemberDTO.of(member);

        return memberDTO;
    }

    // 사용자 정보 조회 -사용자
    @Transactional
    public MemberDetailDTO getMemberProfile(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        List<MemberInterest> memberInterests = memberInterestRepository.findAllByMemberId(memberId);

        List<MemberInterestDTO> memberInterestDTOList = memberInterests.stream()
                .map(MemberInterestDTO::of)
                .toList();

        MemberDetailDTO memberDetailDto = MemberDetailDTO.of(member, memberInterestDTOList);

        return memberDetailDto;
    }

    // 사용자 정보(프로필) 수정
    @Transactional
    public void updateMemberProfile(final Long memberId,
                                    final MemberProfileUpdateRequest memberProfileUpdateRequest,
                                    MultipartFile imageFile) {
        try{
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        String nickname = memberProfileUpdateRequest.getNickname();


        if (nickname != null && !nickname.isBlank()) {
            member.setNickname(nickname);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            member.setProfileImage(imageUrl);
        }

        member.setUpdatedAt(OffsetDateTime.now());


        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }
    }

    // 회원 삭제/탈퇴(관리자/사용자) - 소프트 딜리트
    @Transactional
    public void deleteMember(final Long id) {

        final Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 회원이 없습니다."));

        if (member.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.ALREADY_DELETED_USER);
        }

        member.setStatus("WITHDRAWN");
        member.setDeletedAt(OffsetDateTime.now()); // 삭제 날짜에 현재 시간 입력
    }

    // 관리자 - 특정 유저 블랙 리스트 설정
    @Transactional
    public void setMemberBlackList(Long memberId) {
        try{

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if (member.getIsBlacklisted() == true){
            throw new CommonException(ResponseCode.ALREADY_REGISTERED_BLACKLIST);
        }
            member.setIsBlacklisted(true);

        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }

    }

    // 관리자 - 특정 유저 블랙 리스트 해제
    @Transactional
    public void clearMemberBlackList(Long memberId) {
        try{

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

            if (member.getIsBlacklisted() == false){
                throw new CommonException(ResponseCode.MEMBER_NOT_FOUND);
            }

            member.setIsBlacklisted(false);

        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }
    }

    // 관리자 - 유저 관리자 권한 부여
    @Transactional
    public void setMemberRoleAdmin(Long memberId) {
        try{

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if ("ROLE_ADMIN".equals(member.getRole())) { // 리터럴을 앞에 두어 null 방지
            throw new CommonException(ResponseCode.ALREADY_REGISTERED_ADMIN);
        }

        member.setRole("ROLE_ADMIN");
        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }

    }

    // 관리자 - 유저 관리자 권한 회수
    @Transactional
    public void clearMemberRoleAdmin(Long memberId) {
        try{

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

            if ("ROLE_USER".equals(member.getRole())) { // 리터럴을 앞에 두어 null 방지
                throw new CommonException(ResponseCode.ALREADY_REGISTERED_USER);
            }

            member.setRole("ROLE_USER");

        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }
    }

    // 비밀번호 검증
    public boolean verifyPassword(Long memberId, String password) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CommonException(ResponseCode.INVALID_PASSWORD);
        }

        return member.getPassword().equals(password);
    }



}
