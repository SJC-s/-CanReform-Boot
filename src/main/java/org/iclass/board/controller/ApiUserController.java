package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UserAccountDTO;
import org.iclass.board.dto.UsersDTO;
import org.iclass.board.jwt.TokenProvider;
import org.iclass.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
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

    @GetMapping("/check-userId")
    public ResponseEntity<?> checkUsername(@RequestParam String userId) {
        boolean exists = userService.checkUsernameExists(userId);
        return ResponseEntity.ok(exists);
    }

}