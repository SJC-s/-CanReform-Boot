package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.jwt.JwtTokenUtil;
import org.iclass.board.dto.UserDTO;
import org.iclass.board.entity.UserEntity;
import org.iclass.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiUserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.login(userDTO.getUserId(), userDTO.getPassword());

        if (user != null) {
            // 로그인 성공 JWT 토큰 생성
            String token = jwtTokenUtil.generateToken(user.getUserId());

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } else {
            // 로그인 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
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
