package org.iclass.board.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {
    // 스프링 시큐리티는 서블릿 필터 기반으로 동작하면서 DispatcherServlet 동작하기 전에 시큐리티 설정 내용이 실행된다(면접에서 자주 나옴)
    // DispatcherServlet : 모든 요청을 먼저 접수하고 핸들러 메소드와 매핑
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Custom Security Filter Chain 동작");

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/", "/home", "/login", "/signup", "/css/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/?logout").permitAll())
        ;

        return http.build();
    }
}