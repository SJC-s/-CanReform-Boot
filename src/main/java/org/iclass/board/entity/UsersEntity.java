package org.iclass.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "USERS")
public class UsersEntity {

    @Id
    @Column(name = "USERID", nullable = false, length = 50)
    private String userId;  // 자동 생성되지 않으므로 수동으로 처리

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "ISACTIVE", nullable = false, length = 1)
    private char isActive;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @Column(name = "USERSROLE", nullable = false)
    private String usersRole;

    @CreatedDate
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt = LocalDateTime.now();
}