package org.iclass.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Builder
@Data // 불변객체 관련된 메소드 재정의
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@CrossOrigin(origins = "http://localhost:5173")  // 프론트엔드 주소로 변경
@Table(name = "USERS") // USER 테이블과 매핑
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

    @Column(name = "USERSROLE", nullable = false, columnDefinition = "VARCHAR2(20) DEFAULT 'MEMBER' CHECK (USERSROLE IN ('MEMBER', 'ADMIN'))")
    private String usersRole;

    @CreatedDate
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column()
    private String resetPasswordToken;

    @Column()
    private LocalDateTime resetPasswordExpires;
}
