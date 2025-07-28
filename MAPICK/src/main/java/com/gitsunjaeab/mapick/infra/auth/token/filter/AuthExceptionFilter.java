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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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


        } catch (IllegalArgumentException | JwtException ex) {
            log.warn("❌ 인증 관련 예외 발생: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");


            ApiResponse apiResponse = ApiResponse.of(ResponseCode.UNAUTHORIZED);
            String json = objectMapper.writeValueAsString(apiResponse);
            log.info("➡️ 응답: {}", json);
            response.getWriter().write(json);

        } catch (Exception ex) {
            log.error("❌ 필터 내 처리되지 않은 예외 발생", ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse apiResponse = ApiResponse.of(ResponseCode.INTERNAL_ERROR);
            String json = objectMapper.writeValueAsString(apiResponse);
            log.info("➡️ 응답: {}", json);
            response.getWriter().write(json);
        }
    }

}
