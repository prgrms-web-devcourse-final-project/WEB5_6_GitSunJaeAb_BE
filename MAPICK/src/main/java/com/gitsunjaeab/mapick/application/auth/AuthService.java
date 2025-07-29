package com.gitsunjaeab.mapick.application.auth;

import com.gitsunjaeab.mapick.api.auth.dto.request.SocialLoginRequest;
import com.gitsunjaeab.mapick.api.auth.dto.internal.SocialUserInfo;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.*;
import com.gitsunjaeab.mapick.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.member.code.MemberStatus;
import com.gitsunjaeab.mapick.infra.auth.token.JwtProvider;
import com.gitsunjaeab.mapick.infra.auth.token.code.GrantType;
import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;

import java.time.OffsetDateTime;
import java.util.Optional;

import com.gitsunjaeab.mapick.util.NotFoundException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;


    // 소셜 로그인
    // complete
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

            // 블랙 리스트 회원 인 경우
            if (existing.getIsBlacklisted()){
                throw new CommonException(ResponseCode.BLACKLISTED_USER);
            }

            // 탈퇴된 회원인 경우
            if (MemberStatus.WITHDRAWN.name().equalsIgnoreCase(existing.getStatus())) {
                throw new CommonException(ResponseCode.WITHDRAWN_USER);
            }

            existing.setLastLogin(OffsetDateTime.now()); // last login 날짜 추가
            existing.setLoginCount(existing.getLoginCount() + 1);


            TokenDTO tokendto = processTokenSignin(existing); // jwt 토큰 발급

            return tokendto;

        }

        // 해당 이메일로 가입된 계정이 없는 경우 - 회원 가입

            Member member = memberService.registerNewSocialMember(userInfo, request.getProvider());

            TokenDTO tokendto = processTokenSignin(member); // jwt 토큰 발급

            return tokendto;

    }

    // 자체 로그인
    // complete
    @Transactional
    public TokenDTO signin(String email, String password){

        // 이메일로 사용자 객체 가져오기
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CommonException(ResponseCode.EMAIL_NOT_FOUND));

        // PW 비교
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CommonException(ResponseCode.INVALID_PASSWORD);
        }

        // 블랙 리스트 회원 인 경우
        if (member.getIsBlacklisted()){
            throw new CommonException(ResponseCode.BLACKLISTED_USER);
        }

        // 탈퇴된 회원인 경우
        if (MemberStatus.WITHDRAWN.name().equalsIgnoreCase(member.getStatus())) {
            throw new CommonException(ResponseCode.WITHDRAWN_USER);
        }

        member.setLastLogin(OffsetDateTime.now()); // last login 날짜 추가
        member.setLoginCount(member.getLoginCount() + 1); // 로그인 카운트 1 증가


        authenticateMember(email, password); // 인증 객체 생성 및 Spring Security 등록

        TokenDTO tokendto = processTokenSignin(member); // jwt 토큰 발급

        return tokendto;
    }

    // 비밀번호 변경
    // complete
    @Transactional
    public TokenDTO updatePassword(HttpServletRequest request, Long memberId, String password) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        // 계정에 존재하는 기존 refresh token 삭제
        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);

        // 기존 access token 블랙 리스트 등록
        if (accessToken != null && !accessToken.isBlank()) {

            accessTokenBlacklistRepository.save(new AccessTokenBlacklist(accessToken));
        }

        refreshTokenRepository.deleteByMember(member);

        member.setPassword(passwordEncoder.encode(password));
        member.updateTimestamp();

        authenticateMember(member.getEmail(), password); // 바로 로그아웃 안시키려면 다시 인증

        TokenDTO tokendto = processTokenSignin(member); // JWT 토큰 발급

        return tokendto;
    }

    // 로그아웃
    // complete
    @Transactional
    public void logout(HttpServletRequest request) {

        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);

        Claims claims = jwtProvider.parseClaim(accessToken);
        String jti = claims.getId();


        // 기존 access token 블랙 리스트 등록
        if (accessToken != null && !accessToken.isBlank()) {

            accessTokenBlacklistRepository.save(new AccessTokenBlacklist(accessToken));
        }

        refreshTokenRepository.deleteByjti(jti);

    }

    // 인증 객체 생성 및 Spring Security 등록
    // complete
    public void authenticateMember(String email, String password) {
        // 인증된 사용자 정보 등록용 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password); // 인증되지 않은 토큰
        Authentication authentication = authenticationManager.authenticate(authToken); // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(authentication); // 보안 컨텍스트 저장
    }

    // JWT 토큰 발급
    // complete
    public TokenDTO processTokenSignin(Member member) {

        TokenDTO accessToken = jwtProvider.generateAccessToken(member.getEmail()); // AccessToken 만들기
        RefreshToken refreshToken = new RefreshToken(member, accessToken.getJti()); // RefreshToken 만들기

        try{
            refreshTokenRepository.save(refreshToken); // RefreshToken 저장
        }catch (DataIntegrityViolationException e){
            throw new CommonException(ResponseCode.DB_CONSTRAINT_VIOLATION); // DB 제약 조건 위배
        }

        TokenDTO tokenDto = TokenDTO.builder()
                .accessToken(accessToken.getAccessToken())
                .refreshToken(refreshToken.getRefreshToken())
                .atExpiresIn(jwtProvider.getAtExpiration()) // access token의 만료 시간
                .rtExpiresIn(jwtProvider.getRtExpiration()) // refresh token의 만료 시간
                .grantType(GrantType.BEARER)
                .build();

        return tokenDto;
    }
}