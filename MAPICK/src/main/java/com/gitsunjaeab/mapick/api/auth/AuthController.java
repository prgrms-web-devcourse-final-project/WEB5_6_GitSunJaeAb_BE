package com.gitsunjaeab.mapick.api.auth;

import com.gitsunjaeab.mapick.api.auth.dto.request.SigninRequest;
import com.gitsunjaeab.mapick.api.auth.dto.request.SignupRequest;
import com.gitsunjaeab.mapick.api.auth.dto.request.SocialLoginRequest;
import com.gitsunjaeab.mapick.api.auth.dto.response.SocialTokenResponse;
import com.gitsunjaeab.mapick.api.auth.dto.response.LocalTokenResponse;
import com.gitsunjaeab.mapick.api.auth.dto.response.TokenResponse;
import com.gitsunjaeab.mapick.application.auth.AuthService;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.TokenDTO;
import com.gitsunjaeab.mapick.infra.auth.token.TokenCookieFactory;
import com.gitsunjaeab.mapick.infra.auth.token.code.GrantType;
import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "인증 API", description = "로그인 및 회원가입 관련 API")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    // 소셜 로그인
    @PostMapping("/socialLogin")
    @Operation(summary = "소셜 로그인", description = "첫 소셜 로그인 > 회원가입")
    public ResponseEntity<SocialTokenResponse> socialLogin(@RequestBody SocialLoginRequest request, HttpServletResponse response) {

        TokenDTO dto = authService.registerOrLoginSocialUser(request); // now returns TokenDto

        ResponseCookie accessTokenCookie = TokenCookieFactory.create(
                TokenType.ACCESS_TOKEN.name(),
                dto.getAccessToken(),
                dto.getAtExpiresIn()
        );
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
                TokenType.REFRESH_TOKEN.name(),
                dto.getRefreshToken(),
                dto.getRtExpiresIn()
        );

//        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());


        TokenResponse tokenResponseDto = TokenResponse.builder()
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .expiresIn(dto.getAtExpiresIn())
                .grantType(GrantType.BEARER)
                .build();


        return ResponseEntity.ok(SocialTokenResponse.of(tokenResponseDto));
    }

    // 자체 로그인
    @PostMapping("/signin")
    @Operation(summary = "자체 로그인")
    public ResponseEntity<LocalTokenResponse> signin(@RequestBody SigninRequest request, HttpServletResponse response) {

            TokenDTO dto = authService.signin(request.getEmail(), request.getPassword()); // 인증 정보로 로그인 처리 후, access/refresh 토큰 생성

            // 쿠키 굽기
            ResponseCookie accessTokenCookie = TokenCookieFactory.create(
                    TokenType.ACCESS_TOKEN.name(),
                    dto.getAccessToken(),
                    dto.getAtExpiresIn()
            );
            ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
                    TokenType.REFRESH_TOKEN.name(),
                    dto.getRefreshToken(),
                    dto.getRtExpiresIn()
            );

            // 응답 헤더에 추가
//            response.addHeader("Set-Cookie", accessTokenCookie.toString());
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());

            // 반환용 객체에 accessToken 전달
            TokenResponse tokenResponseDto = TokenResponse.builder()
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .expiresIn(dto.getAtExpiresIn())
                .grantType(GrantType.BEARER)
                .build();

        return ResponseEntity.ok(LocalTokenResponse.of(tokenResponseDto));
    }

    // 자체 회원가입
    @PostMapping("/signup")
    @Operation(summary = "자체 회원가입", 
               description = "[회원가입] 자체 회원가입을 진행합니다.\n\n" +
                           "**검증 조건:**\n" +
                           "- 비밀번호는 8자 이상, 12자 이하 영문+숫자를 포함해야 합니다.\n" +
                           "- 이메일은 중복 불가입니다.\n" +
                           "- 닉네임은 중복 불가입니다.")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {

        memberService.signup(request);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SIGNUP_SUCCESS));

    }

}


