package com.gitsunjaeab.mapick.infra.auth.token.filter;

import com.gitsunjaeab.mapick.application.auth.RefreshTokenService;
import com.gitsunjaeab.mapick.domain.auth.RefreshToken;
import com.gitsunjaeab.mapick.domain.auth.TokenDTO;
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
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludePaths = List.of(
            "/error", "/favicon.ico", "/css", "/img", "/js", "/download",
            "/auth/signup","/index", "/auth/signin","/auth/socialLogin"
        );
        String path = request.getRequestURI();
        return excludePaths.stream().anyMatch(path::startsWith);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestAccessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        if (requestAccessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtProvider.validateToken(requestAccessToken)) {
                Authentication authentication = jwtProvider.generateAuthentication(requestAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            TokenDTO newAccessToken = renewingAccessToken(requestAccessToken, request);
            if (newAccessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            RefreshToken newRefreshToken = renewingRefreshToken(ex.getClaims().getId(), newAccessToken.getId());
            responseToken(response, newAccessToken, newRefreshToken);
        }  catch (Exception e) {
            System.out.println("❌ 기타 JWT 예외 발생: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private TokenDTO renewingAccessToken(String accessToken, HttpServletRequest request) {
        Authentication authentication = jwtProvider.generateAuthentication(accessToken);
        String refreshToken = jwtProvider.resolveToken(request, TokenType.REFRESH_TOKEN);
        Claims claims = jwtProvider.parseClaim(accessToken);

        RefreshToken storedRefreshToken = refreshTokenService.findByAccessTokenId(claims.getId());

        if(storedRefreshToken == null) {
            return null;
        }
        

        TokenDTO newAccessToken = jwtProvider.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return newAccessToken;
    }

    private RefreshToken renewingRefreshToken(String oldAccessTokenId, String newAccessTokenId) {
        return refreshTokenService.renewingToken(oldAccessTokenId, newAccessTokenId);
    }

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
