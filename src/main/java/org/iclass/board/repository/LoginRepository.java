package org.iclass.board.repository;

import org.iclass.board.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
    //LoginEntity findByEmail(String email);
    LoginEntity findByUserid(Long userid);
}
