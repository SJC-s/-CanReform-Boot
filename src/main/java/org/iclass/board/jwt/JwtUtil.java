package org.iclass.board.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class JwtUtil {
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // 요청 헤더에서 토큰 추출
    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
