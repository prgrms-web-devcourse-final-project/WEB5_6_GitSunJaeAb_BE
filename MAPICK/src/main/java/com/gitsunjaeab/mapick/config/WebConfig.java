package com.gitsunjaeab.mapick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring Boot 애플리케이션의 전역 CORS 설정을 정의
// CORS 설정을 통해 특정 도메인에서 오는 요청을 허용
@Configuration // Spring의 설정 클래스라는 것을 의미
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 CORS를 허용
                    .allowedOrigins(
                            "http://localhost:8080",
                            "http://localhost:63342",
                            "http://localhost:3000",
                            "https://localhost:3443",
                            "https://localhost:3000",
                            "https://mapick.cedartodo.uk"
                            ) // 해당 도메인에서 오는 요청만 허용
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // 지정된 HTTP 메서드만 허용
                    .allowedHeaders("*") // 모든 헤더를 허용
                    .allowCredentials(true); // 자격 증명(쿠키, 인증 정보 등)을 허용
            }
        };
    }
}
