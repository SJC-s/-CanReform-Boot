package org.iclass.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/api/check-login")
    public ResponseEntity<Void> checkLogin(HttpSession session) {
        // 세션에서 사용자 정보 확인 (로그인 여부 판단)
        if (session.getAttribute("user") == null) {
            // 로그인이 안 된 경우 401 상태 반환
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 로그인이 된 경우 200 OK 상태 반환
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

