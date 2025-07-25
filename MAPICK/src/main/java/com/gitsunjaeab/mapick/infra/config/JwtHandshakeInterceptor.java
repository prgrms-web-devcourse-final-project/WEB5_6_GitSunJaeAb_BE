package com.gitsunjaeab.mapick.infra.config;

import com.gitsunjaeab.mapick.infra.auth.token.JwtProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    public JwtHandshakeInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        try {
            // 쿼리스트링에서 token 파라미터 추출
            String query = request.getURI().getQuery(); // 예: token=eyJ...
            if (query != null && query.startsWith("token=")) {
                String token = query.substring("token=".length());

                if (jwtProvider.validateToken(token)) {
                    Long userId = jwtProvider.getUserIdFromToken(token);
                    attributes.put("memberId", userId); // 세션 저장
                    return true;
                }
            }
            System.out.println("❌ 토큰 없음 또는 형식 오류: " + query);
            return false;
        } catch (Exception e) {
            System.err.println("❌ Handshake JWT 인증 오류: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // 생략 가능
    }
}