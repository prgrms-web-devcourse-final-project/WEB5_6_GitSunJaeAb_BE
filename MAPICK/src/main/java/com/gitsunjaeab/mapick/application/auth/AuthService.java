package com.gitsunjaeab.mapick.application.auth;

import com.gitsunjaeab.mapick.api.auth.dto.request.SocialLoginRequest;
import com.gitsunjaeab.mapick.api.auth.dto.internal.SocialUserInfo;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.LoginType;
import com.gitsunjaeab.mapick.domain.auth.RefreshToken;
import com.gitsunjaeab.mapick.domain.auth.RefreshTokenRepository;
import com.gitsunjaeab.mapick.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.infra.auth.token.JwtProvider;
import com.gitsunjaeab.mapick.infra.auth.token.code.GrantType;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;

import java.time.OffsetDateTime;
import java.util.Optional;

import com.gitsunjaeab.mapick.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final GoogleOAuthService googleOAuthService;
    private final PasswordEncoder passwordEncoder;


    // 소셜 로그인
    @Transactional
    public TokenDTO socialLogin(SocialLoginRequest request) {

        SocialUserInfo userInfo = googleOAuthService.getUserInfo(request.getToken());
        String email = userInfo.getEmail();

        Optional<Member> optional = memberRepository.findByEmail(email);

        // 해당 이메일로 가입된 계정이 있는 경우 - 로그인
        if (optional.isPresent()) {
            Member existing = optional.get();

            // 이미 로컬로 가입된 이메일일 경우
            if (existing.getLoginType() != LoginType.SOCIAL) {
                throw new CommonException(ResponseCode.EMAIL_ALREADY_REGISTERED_LOCALLY);
            }

            // 이미 다른 provider로 등록 되어 있는 경우
            if (!existing.getProvider().equalsIgnoreCase(request.getProvider())) {
                throw new CommonException(ResponseCode.PROVIDER_MISMATCH);
            }

            TokenDTO tokendto = processTokenSignin(existing); // jwt 토큰 발급

            return tokendto;

        }

        // 해당 이메일로 가입된 계정이 없는 경우 - 회원 가입

            Member member = memberService.registerNewSocialMember(userInfo, request.getProvider());

            TokenDTO tokendto = processTokenSignin(member); // jwt 토큰 발급

            return tokendto;

    }

    // 자체 로그인
    public TokenDTO signin(String email, String password){

        // 이메일로 사용자 객체 가져오기
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CommonException(ResponseCode.EMAIL_NOT_FOUND));

        // PW 비교
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CommonException(ResponseCode.INVALID_PASSWORD);
        }

        member.setLastLogin(OffsetDateTime.now()); // last login 날짜 추가
        member.setLoginCount(member.getLoginCount() + 1);
        memberRepository.save(member);

        authenticateMember(email, password); // 인증 객체 생성 및 Spring Security 등록

        TokenDTO tokendto = processTokenSignin(member); // jwt 토큰 발급

        return tokendto;
    }

    // 비밀번호 변경
    @Transactional
    public TokenDTO updatePassword(Long memberId, String password) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        refreshTokenRepository.deleteByMember(member);

        member.setPassword(passwordEncoder.encode(password));
        member.updateTimestamp();

        authenticateMember(member.getEmail(), password); // 바로 로그아웃 안시키려면 다시 인증

        TokenDTO tokendto = processTokenSignin(member); // JWT 토큰 발급

        return tokendto;
    }

    // 인증 객체 생성 및 Spring Security 등록
    public void authenticateMember(String email, String password) {
        // 인증된 사용자 정보 등록용 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password); // 인증되지 않은 토큰
        Authentication authentication = authenticationManager.authenticate(authToken); // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(authentication); // 보안 컨텍스트 저장
    }

    // JWT 토큰 발급
    public TokenDTO processTokenSignin(Member member) {

        TokenDTO accessToken = jwtProvider.generateAccessToken(member.getEmail()); // AccessToken 만들기
        RefreshToken refreshToken = new RefreshToken(member, accessToken.getId()); // RefreshToken 만들기

        try{
            refreshTokenRepository.save(refreshToken); // RefreshToken 저장
        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }

        TokenDTO tokenDto = TokenDTO.builder()
                .accessToken(accessToken.getAccessToken())
                .refreshToken(refreshToken.getToken())
                .atExpiresIn(jwtProvider.getAtExpiration()) // access token의 만료 시간
                .rtExpiresIn(jwtProvider.getRtExpiration()) // refresh token의 만료 시간
                .grantType(GrantType.BEARER)
                .build();

        return tokenDto;
    }
}