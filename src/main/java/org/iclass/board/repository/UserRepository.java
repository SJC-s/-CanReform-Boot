package org.iclass.board.repository;

import org.iclass.board.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, String> {
    UsersEntity findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    UsersEntity findByEmail(String email);

    UsersEntity findByUserIdAndEmail(String userId, String email);

    Optional<Object> findByResetPasswordToken(String token);

}
