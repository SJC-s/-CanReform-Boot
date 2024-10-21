package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UserDTO;
import org.iclass.board.entity.UserEntity;
import org.iclass.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiUserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.login(userDTO.getUserId(), userDTO.getPassword());

        if (user != null) {
            // 로그인 성공
            return ResponseEntity.ok(user);
        } else {
            // 로그인 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO dto){
        log.info("dtoadfasdfasdf124124124{}", dto.toString());
        UserDTO result = userService.signup(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-userId")
    public ResponseEntity<?> checkUsername(@RequestParam String userId) {
        boolean exists = userService.checkUsernameExists(userId);
        return ResponseEntity.ok(exists);
    }
}
