package com.gitsunjaeab.mapick.infra.auth.token;

import static org.springframework.http.ResponseCookie.from;

import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import org.springframework.http.ResponseCookie;

public class TokenCookieFactory {
    public static ResponseCookie create(String name, String value, Long expires){

        long expiresInSeconds = expires / 1000L;

        return from(name, value)
                .httpOnly(true)
                .maxAge(expiresInSeconds)
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();
    }

    public static ResponseCookie createExpiredToken(TokenType tokenType) {

        return from(tokenType.name(), "")
                .httpOnly(true)
                .maxAge(0) // 유효 시간
                .sameSite("None")
                .secure(true) // true 인거 false 인거 하나씩
                .path("/")
                .build();
    }
}
