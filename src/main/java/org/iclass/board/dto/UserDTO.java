package org.iclass.board.dto;

import lombok.*;
import org.iclass.board.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String usersRole;
    private LocalDateTime createdAt;
    private char isActive;



    /* JPA 구간 */
    public static UserDTO of(UserEntity entity) {
        return UserDTO.builder()
                .email(entity.getEmail())
                .isActive(entity.getIsActive())
                .userId(entity.getUserId())
                .usersRole(entity.getUsersRole())
                .password(entity.getPassword())
                .username(entity.getUsername())
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .usersRole(this.usersRole)
                .isActive(this.isActive)
                .userId(this.userId)
                .password(this.password)
                .username(this.username)
                .build();
    }
}

