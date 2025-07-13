package com.gitsunjaeab.mapick.infra.auth.token;

import static org.springframework.http.ResponseCookie.from;

import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import org.springframework.http.ResponseCookie;

public class TokenCookieFactory {
    public static ResponseCookie create(String name, String value, Long expires){
        return from(name, value)
                   .httpOnly(true)
                   .maxAge(expires)
                   // todo : 배포 후 true 로 변경
                   .secure(false)
                   .path("/")
                   .build();
    }
    
    public static ResponseCookie createExpiredToken(TokenType tokenType) {
        return from(tokenType.name(), "")
                   .httpOnly(true)
                   .maxAge(0)
                   .secure(false)
                   .path("/")
                   .build();
    }
}
