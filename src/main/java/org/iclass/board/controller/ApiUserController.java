package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UsersDTO;
import org.iclass.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class ApiUserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersDTO dto) {
        Map<String, Object> response = userService.login(dto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UsersDTO dto){
        UsersDTO result = userService.signup(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkUserId")
    public ResponseEntity<?> checkUsername(@RequestParam String userId) {
        boolean exists = userService.checkUsernameExists(userId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/getRole/{userId}")
    public ResponseEntity<?> getUsersrole(@PathVariable String userId) {
        String role = userService.findUsersroleByUserId(userId);
        return ResponseEntity.ok().body(Collections.singletonMap("role", role));
    }

    @GetMapping("/findUserId")
    public ResponseEntity<?> findUserId(@RequestParam String email) {
        UsersDTO userId = userService.findUserIdByEmail(email);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/findPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String email = request.get("email");
        try {
            userService.sendResetPasswordEmail(userId, email);
            return ResponseEntity.ok(Collections.singletonMap("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        String userId = request.get("userId");
        try {
            userService.resetPassword(token, newPassword, userId);
            return ResponseEntity.ok(Collections.singletonMap("message", "비밀번호가 성공적으로 재설정되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}