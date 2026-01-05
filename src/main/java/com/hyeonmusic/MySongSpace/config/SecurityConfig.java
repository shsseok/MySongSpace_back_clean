package com.hyeonmusic.MySongSpace.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // security를 적용하지 않을 리소스
        return web -> web.ignoring()
            .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 🔹 모든 요청 허용
            .authorizeHttpRequests(request -> request.anyRequest().permitAll())

            // 🔹 OAuth2 로그인 완전 비활성화
            .oauth2Login(AbstractHttpConfigurer::disable);

        // 🔹 JWT 필터, 인증/인가 핸들러 모두 제거
        // .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        // .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass())
        // .exceptionHandling(...)

        return http.build();
    }
}
