package org.iclass.board.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final TokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // JWT 토큰이 필요한 경로를 설정
        String path = request.getRequestURI();

        // /login 경로로 들어온 요청은 JWT 검증을 하지 않음
        if ("/api/login".equals(path)) {
            chain.doFilter(request, response);  // 다음 필터로 넘어감
            return;
        }

        // 토큰 추출
        String token = JwtUtil.resolveToken(request);

        if (token != null) {
            try {
                // 토큰 검증
                if (tokenProvider.validateToken(token)) {
                    // 토큰으로부터 인증 정보 추출
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    // 시큐리티 컨텍스트에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("토큰 검증 성공 - 사용자: {}", authentication.getName());
                }
            } catch (JwtException | IllegalArgumentException e) {
                // 유효하지 않은 토큰인 경우
                logger.error("유효하지 않은 토큰입니다.", e);
            }
        }

        chain.doFilter(request, response);
    }

}
