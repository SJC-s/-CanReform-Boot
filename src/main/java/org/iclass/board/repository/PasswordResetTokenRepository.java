package org.iclass.board.repository;

import org.iclass.board.dto.UsersDTO;
import org.iclass.board.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    // 토큰으로 엔티티 조회
    PasswordResetToken findByTokenAndUserId(String token, String userId);

    // 특정 사용자의 토큰 삭제
    void deleteByUserId(String userId);
}
