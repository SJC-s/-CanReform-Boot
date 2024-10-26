package org.iclass.board.repository;

import org.iclass.board.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, String> {
    UsersEntity findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);
}
