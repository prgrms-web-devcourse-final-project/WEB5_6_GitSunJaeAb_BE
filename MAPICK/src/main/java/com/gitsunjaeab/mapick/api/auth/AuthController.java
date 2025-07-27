package com.gitsunjaeab.mapick.api.auth;

import com.gitsunjaeab.mapick.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.api.auth.dto.request.SigninRequest;
import com.gitsunjaeab.mapick.api.auth.dto.request.SignupRequest;
import com.gitsunjaeab.mapick.api.auth.dto.request.SocialLoginRequest;
import com.gitsunjaeab.mapick.api.auth.dto.response.LogoutResponse;
import com.gitsunjaeab.mapick.api.auth.dto.response.PasswordChangeResponse;
import com.gitsunjaeab.mapick.api.auth.dto.response.SigninResponse;
import com.gitsunjaeab.mapick.api.auth.dto.response.SignupResponse;
import com.gitsunjaeab.mapick.api.member.dto.request.PasswordRequest;
import com.gitsunjaeab.mapick.application.auth.AuthService;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.domain.auth.AccessTokenBlacklist;
import com.gitsunjaeab.mapick.domain.auth.AccessTokenBlacklistRepository;
import com.gitsunjaeab.mapick.domain.auth.RefreshTokenRepository;
import com.gitsunjaeab.mapick.infra.auth.token.JwtProvider;
import com.gitsunjaeab.mapick.infra.auth.token.TokenCookieFactory;
import com.gitsunjaeab.mapick.infra.auth.token.code.GrantType;
import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "인증 API", description = "로그인 및 회원가입 관련 API")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;


    // 소셜 로그인
    // complete
    @PostMapping("/socialLogin")
    @Operation(summary = "소셜 로그인", description = "첫 소셜 로그인 > 회원가입")
    public ResponseEntity<SigninResponse> socialLogin(@RequestBody SocialLoginRequest request, HttpServletResponse response) {

        TokenDTO dto = authService.socialLogin(request); // 인증 정보로 로그인 처리 후, access/refresh 토큰 생성

        // 쿠키 굽기
        // access token 쿠기 만들기
//        ResponseCookie accessTokenCookie = TokenCookieFactory.create(
//                TokenType.ACCESS_TOKEN.name(),
//                dto.getAccessToken(),
//                dto.getAtExpiresIn()
//        );

        // refresh token 쿠기 만들기
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
                TokenType.REFRESH_TOKEN.name(),
                dto.getRefreshToken(),
                dto.getRtExpiresIn()
        );

        // 응답 헤더에 추가
//      response.addHeader("Set-Cookie", accessTokenCookie.toString()); // 엑세스 토큰 응답 헤더에 추가 -> json 응답으로 반환 되기 때문에 제외
        response.addHeader("Set-Cookie", refreshTokenCookie.toString()); // 리프레시 토큰 응답 헤더에 추가

        // access token & refresh token 반환
        TokenDTO tokenResponseDto = TokenDTO.builder()
                .accessToken(dto.getAccessToken()) // access token 전달
                .refreshToken(dto.getRefreshToken()) // refresh token 전달
                .atExpiresIn(dto.getAtExpiresIn()) // access token 만료 시간 전달
                .grantType(GrantType.BEARER)
                .build();


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SigninResponse.social(tokenResponseDto));
    }

    // 자체 로그인
    // complete
    @PostMapping("/signin")
    @Operation(summary = "자체 로그인")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest request, HttpServletResponse response) {

        TokenDTO dto = authService.signin(request.getEmail(), request.getPassword()); // 인증 정보로 로그인 처리 후, access/refresh 토큰 생성

        // 쿠키 굽기
        // access token 쿠기 만들기
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(
                TokenType.ACCESS_TOKEN.name(),
                dto.getAccessToken(),
                dto.getAtExpiresIn()
        );

        // refresh token 쿠기 만들기
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
                TokenType.REFRESH_TOKEN.name(),
                dto.getRefreshToken(),
                dto.getRtExpiresIn()
        );

        // 응답 헤더에 추가
//      response.addHeader("Set-Cookie", accessTokenCookie.toString()); // 엑세스 토큰 응답 헤더에 추가
        response.addHeader("Set-Cookie", refreshTokenCookie.toString()); // 리프레시 토큰 응답 헤더에 추가

        // access token & refresh token 반환
        TokenDTO tokenResponseDto = TokenDTO.builder()
                .accessToken(dto.getAccessToken()) // access token 전달
                .refreshToken(dto.getRefreshToken()) // refresh token 전달
                .atExpiresIn(dto.getAtExpiresIn()) // access token 만료 시간 전달
                .grantType(GrantType.BEARER)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SigninResponse.local(tokenResponseDto));
    }

    // 자체 회원가입
    // complete
    @PostMapping("/signup")
    @Operation(summary = "자체 회원가입", description = "[회원가입] 자체 회원가입을 진행합니다.\n\n" + "**검증 조건:**\n" + "- 비밀번호는 8자 이상, 12자 이하 영문+숫자를 포함해야 합니다.\n" + "- 이메일은 중복 불가입니다.\n" + "- 닉네임은 중복 불가입니다.")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest request) {

        memberService.signup(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SignupResponse.signup());

    }

    // 로그아웃

    // todo 로그아웃 하지 않으면 refresh token 값이 삭제 되지 않음, 추후 어떻게 기존 db의 refresh 값을 삭제 할 것인지 고민
    // todo 엑세스토큰이 블랙리스트로 저장이 되지 않는 이슈
    @PostMapping("/logout")
    @Operation(summary = "로그 아웃")
    @Transactional
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {

        // 계정에 존재하는 기존 refresh token 삭제 1
        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);

        Claims claims = jwtProvider.parseClaim(accessToken);
        String jti = claims.getId();

        // 기존 access token 블랙 리스트 등록
        if (accessToken != null && !accessToken.isBlank()) {

            accessTokenBlacklistRepository.save(new AccessTokenBlacklist(accessToken));
        }

        refreshTokenRepository.deleteByAccessTokenId(jti);

        // 쿠키 굽기
        ResponseCookie expiredRefreshToken = TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN);

        // 응답 헤더에 추가
        response.addHeader("Set-Cookie", expiredRefreshToken.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(LogoutResponse.logout());
    }

    // 마이페이지 - 비밀번호 수정 (본인만)
    // complete
    @PutMapping("/password")
    @Operation(summary = "비밀번호 수정", description = "[사용자 전용] 본인만 접근 가능한 비밀번호 변경")
    @Transactional
    public ResponseEntity<PasswordChangeResponse> updatePassword(@Valid @RequestBody PasswordRequest passwordRequest,HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(auth.getName());

        // 계정에 존재하는 기존 refresh token 삭제
        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        Claims claims = jwtProvider.parseClaim(accessToken);
        String jti = claims.getId();

        // 기존 access token 블랙 리스트 등록
        if (accessToken != null && !accessToken.isBlank()) {

            accessTokenBlacklistRepository.save(new AccessTokenBlacklist(accessToken));
        }

        refreshTokenRepository.deleteByAccessTokenId(jti);

        TokenDTO tokenDTO = authService.updatePassword(memberId, passwordRequest.getPassword());

        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
                TokenType.REFRESH_TOKEN.name(),
                tokenDTO.getRefreshToken(),
                tokenDTO.getRtExpiresIn()
        );

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        TokenDTO tokenResponseDto = TokenDTO.builder()
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .atExpiresIn(tokenDTO.getAtExpiresIn())
                .grantType(GrantType.BEARER)
                .build();


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PasswordChangeResponse.of(tokenResponseDto));
    }

}


