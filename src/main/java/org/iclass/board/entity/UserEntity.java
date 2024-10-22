package org.iclass.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
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
    @Column(name = "USERID")
    private String userId;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "ISACTIVE", nullable = false, length = 1)
    private char isActive;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    public String password;

    @Column(name = "USERSROLE", nullable = false)
    private String usersRole;

    @CreatedDate
    @Column(name = "CREATEDAT")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
