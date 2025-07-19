package com.gitsunjaeab.mapick.infra.auth.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gitsunjaeab.mapick.application.auth.RefreshTokenService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.auth.token.JwtProvider;
import com.gitsunjaeab.mapick.infra.auth.token.TokenCookieFactory;
import com.gitsunjaeab.mapick.infra.auth.token.code.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);

        if(accessToken == null){
            filterChain.doFilter(request,response);
            return;
        }

        String path = request.getRequestURI();


        if(path.equals("/auth/logout")){

            Claims claims  = jwtProvider.parseClaim(accessToken); // 관련 있는 코드 들이 근처에 있는 것이 좋다
            refreshTokenService.deleteByAccessTokenId(claims.getId());

            ResponseCookie expiredAccessToken = TokenCookieFactory.createExpiredToken(TokenType.ACCESS_TOKEN);
            ResponseCookie expiredRefreshToken = TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN);
            ResponseCookie expiredSessionId = TokenCookieFactory.createExpiredToken(TokenType.AUTH_SERVER_SESSION_ID);

            response.addHeader("Set-Cookie", expiredAccessToken.toString());
            response.addHeader("Set-Cookie", expiredRefreshToken.toString());
            response.addHeader("Set-Cookie", expiredSessionId.toString());
            // 45-51 만료된 토큰 값 넣는 것과, 빈 값 넣는 것과 무슨 차이 인가
            // todo 로그아웃 할때 발급 받은 토큰 만료 시키고 쿠키에서 삭제하고, 재로그인시에 요청 하는지 클라이언트와 상의 필요

            response.setStatus(HttpServletResponse.SC_OK); // 또는 SC_NO_CONTENT
//          response.setContentType("application/json"); // todo 상수로 치환이 가능할 듯 하다.
            response.setContentType(MediaType.APPLICATION_JSON.toString()); // todo 상수로 치환이 가능할 듯 하다.
            response.setCharacterEncoding("UTF-8"); // todo 위에 것 처럼 상수가 있는지 찾아 보기

            ObjectMapper objectMapper = new ObjectMapper(); // todo 매번 생성 시키는 거보다 주이 받아서 사용 하는 것이 좋을 듯 하다.
            objectMapper.registerModule(new JavaTimeModule()); // todo "이거 추가해야 500에러 안뜸", 안뜨는 이유 다시 봐야함, 구글 tasks 앱에 적어두고 공부 필요

//          objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            ApiResponse responseBody = ApiResponse.of(ResponseCode.LOGOUT_SUCCESS);
            String json = objectMapper.writeValueAsString(responseBody); // todo 예외 발생 가능성 있음, 여기서 전파되면 어떻게 될 지, 테스트 필요 , 직렬화 필요한 함수는 트라이-캐치 거는것이 좋다.
            response.getWriter().write(json);

            return; // todo 다음 필터 체인을 안타도 되는 것인지 확인 필요, 마지막이 아니라면 여기에 다음 필터의 dofilter를 거는 것이 좋을 것 같다
        }

        filterChain.doFilter(request,response);

    }
}