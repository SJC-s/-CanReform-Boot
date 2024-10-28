package org.iclass.board.dto;

import lombok.*;
import org.iclass.board.entity.UsersEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UsersDTO {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String usersRole;
    private LocalDateTime createdAt;
    private char isActive;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordExpires;



    /* JPA 구간 */
    public static UsersDTO of(UsersEntity entity) {
        return UsersDTO.builder()
                .email(entity.getEmail())
                .isActive(entity.getIsActive())
                .userId(entity.getUserId())
                .usersRole(entity.getUsersRole())
                .password(entity.getPassword())
                .username(entity.getUsername())
                .resetPasswordToken(entity.getPassword())
                .resetPasswordExpires(entity.getResetPasswordExpires())
                .build();
    }

    public UsersEntity toEntity() {
        return UsersEntity.builder()
                .email(this.email)
                .usersRole(this.usersRole)
                .isActive(this.isActive)
                .userId(this.userId)
                .password(this.password)
                .username(this.username)
                .resetPasswordToken(this.resetPasswordToken)
                .resetPasswordExpires(this.resetPasswordExpires)
                .build();
    }
}