package org.iclass.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "USERS")  // USER 테이블과 매핑
public class LoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long userid;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;
    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "USERSROLE", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'MEMBER'")
    private String usersrole;

    @CreatedDate
    private LocalDateTime created_at;

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private char is_active;
}
