package com.gitsunjaeab.mapick.infra.auth.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("🔥 AuthExceptionFilter 실행됨: {}", request.getRequestURI());

        try {
            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            log.warn("❌ JWT 오류 발생");
            throw new BadCredentialsException("잘못된 JWT 토큰", ex);

        } catch (AuthenticationException ex) {
            log.warn("❌ 인증 예외 발생");
            throw ex;

        }
        catch (IllegalArgumentException ex) {
            log.warn("❌ 잘못된 요청 매개변수 (JWT 없음 또는 빈 문자열)");
//            throw new BadCredentialsException("유효하지 않은 인증 헤더", ex);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse apiResponse = ApiResponse.of(ResponseCode.UNAUTHORIZED); // 커스텀 코드 4010 같은 것
            String json = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(json);

            // 예외 던지지 않고 종료
            return;

        }

        catch (Exception ex) {
            log.warn("❌ 기타 예외 발생");
            throw new AuthenticationServiceException("인증 중 예기치 못한 오류", ex);
        }
    }

}
