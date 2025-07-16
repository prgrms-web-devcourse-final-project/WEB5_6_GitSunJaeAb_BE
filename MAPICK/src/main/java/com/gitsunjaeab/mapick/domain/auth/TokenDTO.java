package com.gitsunjaeab.mapick.domain.auth;

import com.gitsunjaeab.mapick.infra.auth.token.code.GrantType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO { // 내부 서비스/비즈니스 로직 전달용 DTO
    private String id;
    private String accessToken;
    private String refreshToken;
    private GrantType grantType;
    private Long atExpiresIn;
    private Long rtExpiresIn;
}
