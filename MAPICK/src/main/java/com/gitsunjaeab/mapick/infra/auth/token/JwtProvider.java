package com.gitsunjaeab.mapick.infra.auth.token;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String key;

    @Getter
    @Value("${jwt.access-expiration}")
    private long atExpiration; // access token 만료 시간

    @Getter
    @Value("${jwt.refresh-expiration}")
    private long rtExpiration; // refresh token 만료 시간

    private SecretKey secretKey;

    // 비밀 키 만들기
    public SecretKey getSecretKey(){
        if(secretKey == null){
            secretKey  = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        }
        return secretKey;
    }

    // 인증 객체로 JWT 발급
    public TokenDTO generateAccessToken(Authentication authentication){
        String email = ((Principal) authentication.getPrincipal()).getMember().getEmail();
        return generateAccessToken(email);
    }

    // 유저 정보로 JWT 발급
    public TokenDTO generateAccessToken(String email){

        String jti = UUID.randomUUID().toString(); // jti

        long now = new Date().getTime();
        Date atExpiresIn = new Date(now + atExpiration); // 만료일자 생성

        // 이메일로 사용자 객체 가져오기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(ResponseCode.EMAIL_NOT_FOUND));

        List<String> authorities = List.of(member.getRole());

        String accessToken = Jwts.builder()
            .subject(email)
            .id(jti) // jti
            .expiration(atExpiresIn)
            .claim("authorities", authorities)
            .signWith(getSecretKey())
            .compact();

        TokenDTO tokenDto = TokenDTO.builder()
                .jti(jti) // jti
                .accessToken(accessToken)
                .atExpiresIn(atExpiration)
                .build();

        return tokenDto;
    }

    // JWT -> 인증 객체 생성
    public Authentication generateAuthentication(String accessToken) {

        String email = parseClaim(accessToken).getSubject();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole()));

        Principal principal = new Principal(member);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //  Access Token 안에 있는 Claims (페이로드) 꺼내기
    public Claims parseClaim(String accessToken) {
        try {
            return Jwts.parser().verifyWith(getSecretKey()).build()
                    .parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }


    // JWT 유효성 검사(서명 위조, 만료 등)
    public boolean validateToken(String requestAccessToken) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parse(requestAccessToken);
            return true;
        }catch(SecurityException | MalformedJwtException
               | UnsupportedJwtException | IllegalArgumentException
               | ExpiredJwtException e){
            System.out.println("❌ JWT 유효성 검사 실패: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // HTTP 요청에서 특정 토큰 꺼내기
    public String resolveToken(HttpServletRequest request, TokenType tokenType) {
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer ")) {

            return headerToken.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(e -> e.getName().equals(tokenType.name()))
                .map(Cookie::getValue).findFirst()
                .orElse(null);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaim(token);
        Object memberId = claims.get("memberId");

        System.out.println("✅ WebSocket 인증 성공 - memberId: " + memberId);
        if (memberId instanceof Integer) {
            return ((Integer) memberId).longValue();
        } else if (memberId instanceof Long) {
            return (Long) memberId;
        } else {
            throw new CommonException(ResponseCode.MEMBER_NOT_FOUND, "memberId가 토큰에 없습니다.");
        }

    }

    // 서버 간 통신용 토큰 발급
    public String generateServerToken() {
        String id = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Date expiresAt = new Date(now + atExpiration);

        return Jwts.builder()
                .subject("DevQuest-service")
                .id(id)
                .expiration(expiresAt)
                .claim("authorities", List.of("ROLE_SERVER")) // 서버 권한
                .signWith(getSecretKey())
                .compact();
    }
}
