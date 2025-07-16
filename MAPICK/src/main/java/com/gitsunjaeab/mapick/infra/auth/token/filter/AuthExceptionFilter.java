package com.gitsunjaeab.mapick.infra.auth.token.filter;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.AuthWebException;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class AuthExceptionFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;

    public AuthExceptionFilter(
        @Qualifier("handlerExceptionResolver")
        HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CommonException ex) {
            throwAuthEx(request, response, ex.code());
        } catch (JwtException ex) {
            throwAuthEx(request, response, ResponseCode.UNAUTHORIZED);
        }
    }

    private void throwAuthEx(HttpServletRequest request, HttpServletResponse response,
        ResponseCode code) {

        if(code.equals(ResponseCode.INVALID_TOKEN) ||
            code.equals(ResponseCode.SECURITY_INCIDENT)
        ){
            handlerExceptionResolver.resolveException(request, response, null,
                new AuthWebException(code, "/auth/signin"));
            return;
        }

        handlerExceptionResolver.resolveException(request, response, null,
            new AuthWebException(code));
    }
}