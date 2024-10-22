package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UserDTO;
import org.iclass.board.entity.UserEntity;
import org.iclass.board.jwt.TokenProvider;
import org.iclass.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiUserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            // 1. UsernamePasswordAuthenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());

            // 2. AuthenticationManager를 통해 인증 시도
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. 인증이 성공하면 JWT 토큰 생성
            String jwt = tokenProvider.generateToken(authentication);

            // 4. 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("user", authentication.getPrincipal()); // 유저 정보를 담을 수 있음

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            // 5. 인증 실패 시 예외 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO dto){
        UserDTO result = userService.signup(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-userId")
    public ResponseEntity<?> checkUsername(@RequestParam String userId) {
        boolean exists = userService.checkUsernameExists(userId);
        return ResponseEntity.ok(exists);
    }
}