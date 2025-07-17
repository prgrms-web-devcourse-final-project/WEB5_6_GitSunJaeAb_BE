package com.gitsunjaeab.mapick.application.member;

import com.gitsunjaeab.mapick.api.auth.dto.SocialUserInfo;
import com.gitsunjaeab.mapick.api.auth.dto.request.SignupRequest;
import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.api.member.dto.request.MemberProfileUpdateRequest;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberListResponse;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberProfileResponse;
import com.gitsunjaeab.mapick.api.member.dto.response.MemberResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.LoginType;
import com.gitsunjaeab.mapick.domain.auth.Role;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.util.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final PasswordEncoder passwordEncoder;

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
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();

        try{
            memberRepository.save(member);
        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }
    }

    public MemberListResponse findAll() {

        final List<Member> members = memberRepository.findAll(Sort.by("id"));

        return MemberListResponse.of(members);
    }

    // 멤버 상세 조회
    public MemberResponse getMember(final Long memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        return MemberResponse.of(member);
    }

    public Long create(final MemberDTO memberDTO) {
        final Member member = new Member();
        DTOToEntity(memberDTO, member);
        return memberRepository.save(member).getId();
    }


    // 사용자 정보 수정
    @Transactional
    public void updateMemberProfile(final Long memberId, final MemberProfileUpdateRequest memberProfileUpdateRequest) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        member.setNickname(memberProfileUpdateRequest.getNickname());
        member.setProfileImage(memberProfileUpdateRequest.getProfileImage());
        member.setUpdatedAt(OffsetDateTime.now());

        try{
            memberRepository.save(member);
        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }
    }

    // 회원 삭제/탈퇴(소프트 딜리트)
    @Transactional
    public void deleteMember(final Long id) {

        final Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 회원이 없습니다."));

        if (member.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.ALREADY_DELETED_USER);
        }

        member.setDeletedAt(OffsetDateTime.now()); // 삭제 날짜에 현재 시간 입력
    }



    public boolean emailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCase(email);
    }

    // 마이페이지 - 회원 정보 조회
    public MemberProfileResponse getMemberProfile(Long memberId) {

         Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        return MemberProfileResponse.of(member);
    }


    // 마이페이지 - 비밀번호 확인
    public boolean verifyPassword(Long memberId, String currentPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
        
        // 실제 구현에서는 암호화된 비밀번호와 비교해야 함
        return member.getPassword().equals(currentPassword);
    }

    // 마이페이지 - 비밀번호 수정
    public void updatePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        // 실제 구현에서는 비밀번호를 암호화해야 함
        member.setPassword(newPassword);
        member.updateTimestamp();
        memberRepository.save(member);
    }

    // 마이페이지 - 회원 탈퇴
    public void withdrawMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        member.setDeletedAt(OffsetDateTime.now());
        member.setStatus("WITHDRAWN");
        memberRepository.save(member);
    }

    // 마이페이지 - 회원 지도 목록 조회
    public List<Roadmap> getMemberRoadmaps(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        return roadmapRepository.findByMember(member);
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

    // 관리자 - 특정 유저 관리자 설정
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

    // 엔티티 -> dto
    private MemberDTO enityToDTO(final Member member, final MemberDTO memberDTO) {
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setLoginType(member.getLoginType().name());
        memberDTO.setProvider(member.getProvider());
        memberDTO.setRole(member.getRole());
        memberDTO.setStatus(member.getStatus());
        memberDTO.setProfileImage(member.getProfileImage());
        memberDTO.setLastLogin(member.getLastLogin());
        memberDTO.setCreatedAt(member.getCreatedAt());
        memberDTO.setUpdatedAt(member.getUpdatedAt());
        memberDTO.setDeletedAt(member.getDeletedAt());
        return memberDTO;
    }

    // dto -> 엔티티
    private Member DTOToEntity(final MemberDTO memberDTO, final Member member) {
        member.setName(memberDTO.getName());
        member.setNickname(memberDTO.getNickname());
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setLoginType(LoginType.valueOf(memberDTO.getLoginType()));
        member.setProvider(memberDTO.getProvider());
        member.setRole(memberDTO.getRole());
        member.setStatus(memberDTO.getStatus());
        member.setProfileImage(memberDTO.getProfileImage());
        member.setLastLogin(memberDTO.getLastLogin());
        member.setCreatedAt(memberDTO.getCreatedAt());
        member.setUpdatedAt(memberDTO.getUpdatedAt());
        member.setDeletedAt(memberDTO.getDeletedAt());
        return member;
    }

}
