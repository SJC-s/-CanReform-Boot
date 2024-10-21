package org.iclass.board.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.jwt.JwtAuthenticationFilter;
import org.iclass.board.jwt.JwtAuthorizationFilter;
import org.iclass.board.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Slf4j
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private static final String[] PERMIT_LIST = {
            "/", "/signup", "/login", "/js/**", "/api/**", "/index", "/favicon.ico", "/error", "/css/**",
            "/posts"
    };

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        log.info(":::::: Custom Security Filter Chain 작동중");
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(r -> r
                        .requestMatchers(PERMIT_LIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );

        //세션 관리 상태 없음으로 구성, 기존 아이디와 패스워드 인증으로 세션을 쿠키에 유지하는 방식을 사용하지 않음
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
