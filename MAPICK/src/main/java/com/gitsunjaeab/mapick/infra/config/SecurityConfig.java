package com.gitsunjaeab.mapick.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.auth.UserDetailsServiceImpl;
import com.gitsunjaeab.mapick.infra.auth.token.filter.AuthExceptionFilter;
import com.gitsunjaeab.mapick.infra.auth.token.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableCaching
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthExceptionFilter authExceptionFilter;
//    private final LogoutFilter logoutFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // 교차 출처 요청 허용
            .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
            .formLogin(AbstractHttpConfigurer::disable) // 기본 인증 비활성화
            .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화
            .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP basic 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
            .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, accessDeniedException) -> { // 인가 실패 처리
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    ApiResponse apiResponse = ApiResponse.of(ResponseCode.FORBIDDEN);
                    log.info(objectMapper.writeValueAsString(apiResponse));
                    String json = objectMapper.writeValueAsString(apiResponse);
                    response.getWriter().write(json);
                })
                .authenticationEntryPoint((request, response, authException) -> { // 인증 실패 처리
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    ApiResponse apiResponse = ApiResponse.of(ResponseCode.UNAUTHORIZED);
                    log.info(objectMapper.writeValueAsString(apiResponse));
                    log.info(apiResponse.toString());
                    String json = objectMapper.writeValueAsString(apiResponse);
                    response.getWriter().write(json);
                })
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/index", "/auth/signin", "/auth/signup", "/auth/socialLogin").anonymous() // 로그인 하지 않은 사용자만 접근 가능

                .requestMatchers(HttpMethod.POST, "/auth/signin", "/auth/signup" , "/auth/socialLogin","/auth/logout").permitAll() // 누구나 이 URL로 요청 가능

                .requestMatchers("/swagger-ui/**","/v3/api-docs/**", "/ws/**").permitAll() // 누구나 이 URL로 요청 가능
//                .requestMatchers("/home","/members/**", "/interests/**", "/mypage/**","/maps/**","/layers/**","/markers/**").hasAnyRole("USER", "ADMIN") // 사용자페이지 접근 권한
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return builder.build();
    }

    // CORS 관련 보안 정책 추가
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOriginPatterns(List.of(
//                "http://localhost:3000",
//                "https://localhost:3000"
//        ));// 허용할 주소
//        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); //허용 메소드
//        corsConfig.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더  허용
//        corsConfig.setAllowCredentials(true); // 쿠키 포함 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig); // 모든 경로에 적용
//        return source;
//    }

}