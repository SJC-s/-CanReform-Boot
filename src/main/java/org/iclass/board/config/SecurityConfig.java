package org.iclass.board.config;

import jakarta.servlet.Filter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

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
    private final WebMvcConfig webMvcConfig;

    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        log.info(":::::: Custom Security Filter Chain 작동중");
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 관련 설정
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(r -> r
                        .requestMatchers(PERMIT_LIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );

        //세션 관리 상태 없음으로 구성, 기존 아이디와 패스워드 인증으로 세션을 쿠키에 유지하는 방식을 사용하지 않음
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)).apply(new MyCustomDsl());  // JWT 필터 추가;

        return http.build();
    }

    // CORS 관련 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://192.168.0.25:5173")); // frontend url
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 커스텀 필터 설정
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            // JwtAuthenticationFilter 인스턴스 생성
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, tokenProvider);
            jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
            // JwtAuthorizationFilter 인스턴스 생성
            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, tokenProvider);
            // 필터 체인에 필터 추가
            http
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}
