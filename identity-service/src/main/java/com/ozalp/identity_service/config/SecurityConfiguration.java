package com.ozalp.identity_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URLS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api/auth/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // API kullanımı için CSRF'yi kapatın
                .csrf(csrf -> csrf.disable())

                // **GEÇİCİ OLARAK TÜM İSTEKLERE İZİN VEREN KURAL:**
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Tüm URL'lere tam yetki
                )

                // Varsayılan giriş formlarını ve temel kimlik doğrulamasını kapatın
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}