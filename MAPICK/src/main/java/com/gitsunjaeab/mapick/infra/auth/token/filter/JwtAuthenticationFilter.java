package com.gitsunjaeab.mapick.infra.auth.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitsunjaeab.mapick.application.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.application.domain.auth.RefreshTokenService;
import com.gitsunjaeab.mapick.infra.common.response.ApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.auth.AccessTokenBlacklistRepository;
import com.gitsunjaeab.mapick.application.domain.auth.Principal;
import com.gitsunjaeab.mapick.application.domain.auth.RefreshToken;
import com.gitsunjaeab.mapick.infra.auth.token.JwtProvider;
import com.gitsunjaeab.mapick.infra.auth.token.TokenCookieFactory;
import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludePaths = List.of(
                "/error", "/favicon.ico", "/css", "/img", "/js", "/download",
                "/swagger-ui", "/swagger-ui/", "/swagger-ui/index.html",
                "/v3/api-docs",
                "/v3/api-docs/",
                "/v3/api-docs/swagger-config",
                "/ws",
                "/auth/signin", "/auth/signup", "/auth/socialLogin"
        );
        String path = request.getRequestURI();
        return excludePaths.stream().anyMatch(path::startsWith);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestAccessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN); // access token 추출

        try {

            if (requestAccessToken == null || requestAccessToken.isBlank()) { // 비어있는 access token이 오는 경우
                filterChain.doFilter(request, response);
                return;
            }

            if (accessTokenBlacklistRepository.existsByToken(requestAccessToken)) { // access token이 블랙리스트 등록 토큰 거부
                throw new JwtException("블랙리스트에 등록된 액세스 토큰입니다.");
            }

            if (jwtProvider.validateToken(requestAccessToken)) { // access token 검증
                Authentication authentication = jwtProvider.generateAuthentication(requestAccessToken); // access token 으로 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 객체 등록
            }

        } catch (ExpiredJwtException ex) { // access token이 만료된 경우

            TokenDTO newAccessToken = renewingAccessToken(requestAccessToken, request); // access token 재생성

            RefreshToken newRefreshToken = renewingRefreshToken(ex.getClaims().getId(), newAccessToken.getJti()); // 구 jti , 신 jti -> 새로운 refresh token 발급

            responseToken(response, newAccessToken, newRefreshToken);

            return;

        } catch (Exception ex) {
            throw ex;
        }

        filterChain.doFilter(request, response);
    }

    // access token 재생성
    private TokenDTO renewingAccessToken(String requestAccessToken, HttpServletRequest request) {

        Authentication authentication = jwtProvider.generateAuthentication(requestAccessToken); // 인증 객체 생성
        Claims claims = jwtProvider.parseClaim(requestAccessToken); // Claim parser
        String jti = claims.getId();
        log.info("🔍 요청 토큰 JTI: {}", jti);
        log.info("👀 email : {}", ((Principal) authentication.getPrincipal()).getMember().getEmail());

        try {
            refreshTokenService.findByAccessTokenId(jti); // jti 로 refresh token 확인
        } catch (JwtException e) {
            throw new JwtException("❌ 저장된 Refresh Token이 없습니다. 재로그인 필요.");
        }

        TokenDTO newAccessToken = jwtProvider.generateAccessToken(authentication); // 인증 객체로 access token 재생성
        SecurityContextHolder.getContext().setAuthentication(authentication); // 컨텍스트 홀더에 저장

        return newAccessToken;
    }

    // refresh token 재생성
    private RefreshToken renewingRefreshToken(String oldAccessTokenId, String newAccessTokenId) {
        return refreshTokenService.renewingToken(oldAccessTokenId, newAccessTokenId);
    }

    // 응답 쿠키 생성 + json 응답
    private void responseToken(HttpServletResponse response, TokenDTO at, RefreshToken rt) {
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(
                TokenType.ACCESS_TOKEN.name(), at.getAccessToken(), at.getAtExpiresIn()
        );
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
                TokenType.REFRESH_TOKEN.name(), rt.getRefreshToken(), jwtProvider.getRtExpiration()
        );

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        TokenDTO tokenResponseDto = TokenDTO.builder()
                .accessToken(at.getAccessToken())
                .refreshToken(rt.getRefreshToken())
                .atExpiresIn(at.getAtExpiresIn())
                .rtExpiresIn(jwtProvider.getRtExpiration())
                .grantType(at.getGrantType())
                .build();

        ApiResponse<TokenDTO> apiResponse = ApiResponse.of(ResponseCode.TOKEN_REISSUED, tokenResponseDto);
        try {
            String json = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("❌ JSON 직렬화 또는 응답 write 실패", e);
            throw new RuntimeException(e);
        }
    }


}


