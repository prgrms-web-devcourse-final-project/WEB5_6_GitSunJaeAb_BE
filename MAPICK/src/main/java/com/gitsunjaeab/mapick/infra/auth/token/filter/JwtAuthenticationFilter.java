package com.gitsunjaeab.mapick.infra.auth.token.filter;

import com.gitsunjaeab.mapick.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.application.auth.RefreshTokenService;
import com.gitsunjaeab.mapick.domain.auth.AccessTokenBlacklistRepository;
import com.gitsunjaeab.mapick.domain.auth.RefreshToken;
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
    private final AccessTokenBlacklistRepository  accessTokenBlacklistRepository;

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

            if (requestAccessToken == null || requestAccessToken.isBlank()) { // 빈 jtw가 오는 경우
//                throw new IllegalArgumentException("Access Token is missing or empty.");
                filterChain.doFilter(request, response);
                return;

            }

            if (accessTokenBlacklistRepository.existsByToken(requestAccessToken)) {
                throw new JwtException("블랙리스트에 등록된 액세스 토큰입니다.");
            }

            if (jwtProvider.validateToken(requestAccessToken)) {
                Authentication authentication = jwtProvider.generateAuthentication(requestAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException ex) { // 만료된 토큰

            TokenDTO newAccessToken = renewingAccessToken(requestAccessToken, request);

            RefreshToken newRefreshToken = renewingRefreshToken(ex.getClaims().getId(), newAccessToken.getId());
            responseToken(response, newAccessToken, newRefreshToken);

        }  catch (Exception ex) {
            throw ex;
        }

        filterChain.doFilter(request, response);
    }

    // access token 재생성
    private TokenDTO renewingAccessToken(String accessToken, HttpServletRequest request) {
        Authentication authentication = jwtProvider.generateAuthentication(accessToken);
        String refreshToken = jwtProvider.resolveToken(request, TokenType.REFRESH_TOKEN);
        Claims claims = jwtProvider.parseClaim(accessToken);

        try{
            refreshTokenService.findByAccessTokenId(claims.getId());
        }catch (JwtException e){
            throw new JwtException("❌ 저장된 Refresh Token이 없습니다. 재로그인 필요.");
        }

        TokenDTO newAccessToken = jwtProvider.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return newAccessToken;
    }

    // 응답 토큰 재생성 및 저장
    private RefreshToken renewingRefreshToken(String oldAccessTokenId, String newAccessTokenId) {
        return refreshTokenService.renewingToken(oldAccessTokenId, newAccessTokenId);
    }

    // 응답 쿠키 생성
    private void responseToken(HttpServletResponse response, TokenDTO at, RefreshToken rt) {
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(
            TokenType.ACCESS_TOKEN.name(), at.getAccessToken(), at.getAtExpiresIn()
        );
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
            TokenType.REFRESH_TOKEN.name(), rt.getToken(), jwtProvider.getRtExpiration()
        );

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
