package org.iclass.board.controller;

import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UsersDTO;
import org.iclass.board.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.gson.GsonFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiAuthController {

    private final UserService userService;

    @Value("${google.secret}")
    private String googleKey;

    @PostMapping("/googleLogin")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleKey))
                .build();
        try {
            log.info("verifier {}", verifier.verify(token));
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                // 사용자 정보를 가져옵니다.
                String userId = payload.getSubject(); // Google 계정 고유 ID
                String email = payload.getEmail();
                String password = "oauth_dummy_password" + userId;
                String name = (String) payload.get("name");

                // 사용자 인증 및 회원가입 처리
                // 예: DB 조회 후 회원이 아니면 등록, 회원이면 토큰 발급 등

                // 예시: 로그인 성공 메시지 반환
                // UserService의 existsByEmail로 회원 여부 확인
                if (userService.existsByEmail(email)) {
                    // 기존 회원인 경우 로그인 처리
                    Map<String, Object> response = userService.login(UsersDTO.builder().userId(userId).password(password).build());
                    return ResponseEntity.ok(response);
                } else {
                    // 신규 회원인 경우 Builder 패턴을 사용하여 UsersDTO 생성
                    UsersDTO newUser = UsersDTO.builder()
                            .userId(userId)
                            .username(name)
                            .email(email)
                            .password(password) // 소셜 로그인 사용자이므로 임의의 비밀번호 설정
                            .usersRole("MEMBER") // 기본 역할 설정
                            .createdAt(LocalDateTime.now())
                            .isActive('Y') // 활성 사용자
                            .build();

                    // 회원가입 메서드 호출
                    userService.signup(newUser);
                    return ResponseEntity.ok(userService.login(newUser));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
