package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UsersDTO;
import org.iclass.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.iclass.board.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "User Management", description = "Operations related to user management such as registration, login, and profile updates")
public class ApiUserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Allows a user to log in with username and password.")
    public ResponseEntity<?> login(@RequestBody UsersDTO dto) {
        Map<String, Object> response = userService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Registers a new user in the system.")
    public ResponseEntity<?> signup(@RequestBody UsersDTO dto) {
        UsersDTO result = userService.signup(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkUserId")
    @Operation(summary = "Check if userId exists", description = "Checks if the provided user ID is already in use.")
    public ResponseEntity<?> checkUsername(@RequestParam String userId) {
        boolean exists = userService.checkUsernameExists(userId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/checkEmail")
    @Operation(summary = "Check if email exists", description = "Checks if the provided email is already in use.")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/getRole/{userId}")
    @Operation(summary = "Get user role", description = "Retrieves the role of the specified user.")
    public ResponseEntity<?> getUsersrole(@PathVariable String userId) {
        String role = userService.findUsersroleByUserId(userId);
        return ResponseEntity.ok().body(Collections.singletonMap("role", role));
    }

    @GetMapping("/findUserId")
    @Operation(summary = "Find userId by email", description = "Finds a user ID based on the provided email address.")
    public ResponseEntity<?> findUserId(@RequestParam String email) {
        UsersDTO userId = userService.findUserIdByEmail(email);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/findPassword")
    @Operation(summary = "Forgot password", description = "Sends a reset password link to the user's email.")
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
    @Operation(summary = "Reset password", description = "Resets the user's password using a provided token.")
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

    // Mypage 사용자 정보 조회
    @GetMapping("/user")
    @Operation(summary = "Get current user info", description = "Retrieves information about the currently logged-in user.")
    public ResponseEntity<UsersDTO> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // userId를 가져옴 (보통 사용자 ID가 username에 저장됨)

        UsersDTO user = userService.getCurrentUserInfo(userId);
        return ResponseEntity.ok(user);
    }

    // 사용자 정보 수정
    @PutMapping("/user")
    @Operation(summary = "Update user info", description = "Updates information about the current user.")
    public ResponseEntity<String> updateUser(@RequestBody UsersDTO userUpdateDTO, @RequestHeader("Authorization") String token) {
        String userId = extractUserIdFromToken(token);
        userService.updateUser(userId, userUpdateDTO);
        return ResponseEntity.ok("사용자 정보가 성공적으로 업데이트되었습니다.");
    }

    // 토큰에서 사용자 ID를 추출하는 메서드
    private String extractUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        token = token.substring(7); // "Bearer " 부분을 제거
        return tokenProvider.getUserIdFromToken(token); // TokenProvider에서 사용자 ID를 추출
    }

    // 회원 탈퇴
    @DeleteMapping("/user")
    @Operation(summary = "Delete current user", description = "Deletes the currently logged-in user's account.")
    public ResponseEntity<?> deleteCurrentUser(Authentication authentication) {
        try {
            String userId = authentication.getName(); // 로그인한 사용자의 ID
            userService.deleteUserById(userId);
            return ResponseEntity.ok(Collections.singletonMap("message", "회원 탈퇴가 완료되었습니다."));
        } catch (Exception e) {
            log.error("회원 탈퇴 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "회원 탈퇴에 실패했습니다."));
        }
    }
}
