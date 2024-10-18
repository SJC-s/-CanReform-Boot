package org.iclass.board.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UserDTO;
import org.iclass.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

   @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html
    }


    @PostMapping("/login")
    public String login(UserDTO user, Model model) {
        UserDTO authenticatedUser = userService.login(user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            model.addAttribute("errorMessage", "사용자 이름이나 비밀번호가 올바르지 않습니다.");
            return "login"; // 로그인 폼으로 다시 리턴
        }

        // 로그인 성공 후 처리 (예: 세션에 사용자 정보 저장)
        return "redirect:/home"; // 로그인 후 이동할 페이지
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO dto){
        log.info("dtoadfasdfasdf124124124{}", dto.toString());
        UserDTO result = userService.signup(dto);
        return ResponseEntity.ok(result);
    }

/*    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 로그아웃 시 세션에서 사용자 정보 제거
        session.invalidate();
        return "redirect:/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
    }*/


    /*    @GetMapping({"/", "/index"})
    public String index(Model model, HttpServletRequest request, Authentication authentication) {

        // 세션 ID 확인
        HttpSession session = request.getSession();
        log.info("로그인 후 세션 ID: {}", session.getId());

        log.info("사용자 인증 : {}", authentication);
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            model.addAttribute("user", authentication.getName());
        } else {
            log.info("사용자 인증 정보 없음");
        }
        return "index";
    }*/

/*    @GetMapping("/login")
    public String login(String error, Model model){
        if(error != null && error.isEmpty()) {
            model.addAttribute("error", "계정 정보가 일치하지 않습니다.");
            log.info("error : {}", error);
        }
        return "login";
    }*/

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/posts")
    public String bulletinBoard(){
        return "posts";
    }

}




/*public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signup"; // signup.html
    }


    @PostMapping("/signup")
    public String registerUser(UserDTO user, Model model) {
        userService.registerUser(user);
        model.addAttribute("successMessage", "회원가입이 완료되었습니다.");
        return "login"; // login.html
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html
    }


    @PostMapping("/login")
    public String login(UserDTO user, Model model) {
        UserDTO authenticatedUser = userService.login(user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            model.addAttribute("errorMessage", "사용자 이름이나 비밀번호가 올바르지 않습니다.");
            return "login"; // 로그인 폼으로 다시 리턴
        }

        // 로그인 성공 후 처리 (예: 세션에 사용자 정보 저장)
        return "redirect:/home"; // 로그인 후 이동할 페이지
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 로그아웃 시 세션에서 사용자 정보 제거
        session.invalidate();
        return "redirect:/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
    }




    @PostMapping("/api//login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            // 로그인 성공 시 JWT 또는 세션 생성 (예: JWT Token 생성)
            String token = jwtService.createToken(user);
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 사용자 정보가 일치하지 않습니다.");
        }
    }

}*/
