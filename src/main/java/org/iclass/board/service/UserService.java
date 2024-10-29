package org.iclass.board.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UsersDTO;
import org.iclass.board.entity.PasswordResetToken;
import org.iclass.board.entity.UsersEntity;
import org.iclass.board.jwt.TokenProvider;
import org.iclass.board.repository.PasswordResetTokenRepository;
import org.iclass.board.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    public Map<String, Object> login(UsersDTO dto) {

        // 1. UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword());

        // 2. AuthenticationManager를 통해 인증 시도
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증이 성공하면 JWT 토큰 생성
        String jwt = tokenProvider.generateToken(authentication);

        // 4. 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", authentication.getPrincipal()); // 유저 정보를 담을 수 있음

        // 아이디를 기반으로 사용자 찾기
        UsersEntity user = userRepository.findByUserId(dto.getUserId());

        // 사용자가 존재하고, 비밀번호가 일치하면 로그인 성공
        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return response;
        }

        // 로그인 실패 시 null 반환
        return null;
    }


    public UsersDTO signup(UsersDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        UsersEntity entity = dto.toEntity();
        userRepository.save(entity);

        return UsersDTO.of(entity);
    }

    public boolean checkUsernameExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UsersDTO findUserIdByEmail(String email) {
        if(userRepository.findByEmail(email) != null) {
            UsersEntity user = userRepository.findByEmail(email);
            return UsersDTO.of(user);
        } else {
            return null;
        }
    }

    public void sendResetPasswordEmail(String userId, String email) {
        UsersEntity user = userRepository.findByUserIdAndEmail(userId, email);
        if (user == null) {
            throw new IllegalArgumentException("입력한 정보로 사용자를 찾을 수 없습니다.");
        }

        // 기존의 토큰 삭제 (중복 방지)
        tokenRepository.deleteByUserId(user.getUserId());

        // 비밀번호 재설정 토큰 생성 (UUID 사용)
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(user.getUserId(), token, user.getUsername(), LocalDateTime.now().plusHours(1));
        tokenRepository.save(passwordResetToken);

        // 비밀번호 재설정 링크 생성
        String resetLink = "http://localhost:5173/resetPassword?token=" + token;

        // 이메일 전송 예외 처리 추가
        try {
            sendEmail(user.getEmail(), resetLink);
        } catch (MessagingException e) {
            throw new IllegalStateException("이메일 발송 중 오류가 발생했습니다. 다시 시도해주세요.", e);
        }
    }

    public void resetPassword(String token, String newPassword, String userId) {
        PasswordResetToken passwordResetToken = tokenRepository.findByTokenAndUserId(token, userId);
        if (passwordResetToken == null || passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다.");
        }

        UsersEntity user = userRepository.findByUserId(passwordResetToken.getUserId());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 사용된 토큰 삭제 (보안 목적)
        tokenRepository.delete(passwordResetToken);
    }

    private void sendEmail(String toEmail, String resetLink) throws MessagingException {
        // MimeMessage 객체 생성
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // MimeMessageHelper를 사용하여 이메일 작성
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(toEmail);
        helper.setSubject("비밀번호 재설정 안내");
        helper.setText(
                "<p>비밀번호 재설정을 요청하셨습니다.</p>" +
                        "<p>아래 링크를 클릭하여 비밀번호를 재설정하세요:</p>" +
                        "<a href=\"" + resetLink + "\">비밀번호 재설정하기</a>", true);

        // 이메일 발송
        mailSender.send(mimeMessage);
    }

    // 사용자 정보 조회
    public UsersDTO getCurrentUserInfo(String userId) {
        log.info("Searching for user with ID: " + userId); // 사용자 검색 로그
        UsersEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        return UsersDTO.of(user);
    }


    // 사용자 정보 수정
    public void updateUser(String userId, UsersDTO userUpdateDTO) {
        UsersEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        updateUserInfo(user, userUpdateDTO);
        userRepository.save(user);
    }

    // 공통 사용자 정보 업데이트 로직
    private void updateUserInfo(UsersEntity user, UsersDTO userDto) {
        // 비밀번호가 변경되었다면 인코딩 후 업데이트
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        // 필요한 다른 필드들도 업데이트 가능
    }

    // 회원 탈퇴
    public void deleteUserById(String userId) {
        UsersEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        userRepository.delete(user);
    }

}
