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
@Data     // 불변객체 관련된 메소드 재정의
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "USERS")  // USER 테이블과 매핑
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long user_id;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private char is_active;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    public String password;

    @Column(name = "USERSROLE", nullable = false)
    private String usersrole;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime created_at;
}
