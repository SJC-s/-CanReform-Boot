package org.iclass.board.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;
import javax.crypto.SecretKey;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // 시크릿 키 생성
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // 표준 Base64로 인코딩
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Generated Base64 Key: " + base64Key);
    }
}
